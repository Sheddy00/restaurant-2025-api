package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.PostgresNextValId;
import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.dao.mapper.IngredientMapper;
import edu.hei.school.restaurant.model.Price;
import edu.hei.school.restaurant.model.StockMovement;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import edu.hei.school.restaurant.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IngredientCrudOperations implements CrudOperations<Ingredient> {
    private final DataSource dataSource;
    private final IngredientMapper ingredientMapper;
    private final PriceCrudOperations priceCrudOperations;
    private final StockMovementCrudOperations stockMovementCrudOperations;
    final PostgresNextValId nextValId = new PostgresNextValId();

    // TODO : default values for page and size
    @Override
    public List<Ingredient> getAll(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select i.id, i.name from ingredient i order by i.id asc limit ? offset ?")) {
            statement.setInt(1, size);
            statement.setInt(2, size * (page - 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Ingredient ingredient = ingredientMapper.apply(resultSet);
                    ingredients.add(ingredient);
                }
                return ingredients;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public Ingredient findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select i.id, i.name, di.id as dish_ingredient_id, di.required_quantity, di.unit from ingredient i"
                             + " left join dish_ingredient di on i.id = di.id_ingredient"
                             + " where i.id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return ingredientMapper.apply(resultSet);
                }
                throw new NotFoundException("Ingredient.id=" + id + " not found");
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @SneakyThrows
    @Override
    public List<Ingredient> saveAll(List<Ingredient> entities) {
        List<Ingredient> ingredients = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            entities.forEach(entityToSave -> {
                try (PreparedStatement statements =
                             connection.prepareStatement("insert into ingredient (id, name) values (?, ?)"
                                     + " on conflict (id) do update set name=excluded.name"
                                     + " returning id, name")) {

                    long id = entityToSave.getId() == null ? nextValId.nextID("ingredient", connection) : entityToSave.getId();
                    statements.setLong(1, id);
                    statements.setString(2, entityToSave.getName());
                    ResultSet resultSet = statements.executeQuery();

                    if (resultSet.next()) {
                        Ingredient savedIngredient = ingredientMapper.apply(resultSet);
                        List<Price> prices = priceCrudOperations.saveAll(entityToSave.getPrices());
                        List<StockMovement> stockMovements = stockMovementCrudOperations.saveAll(entityToSave.getStockMovements());
                        savedIngredient.addPrices(prices);
                        savedIngredient.addStockMovements(stockMovements);
                        ingredients.add(savedIngredient);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return ingredients;
    }

}
