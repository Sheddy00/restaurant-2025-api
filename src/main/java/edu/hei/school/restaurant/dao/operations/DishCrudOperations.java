package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.PostgresNextValId;
import edu.hei.school.restaurant.dao.mapper.DishMapper;
import edu.hei.school.restaurant.model.Dish;
import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import edu.hei.school.restaurant.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishCrudOperations implements CrudOperations<Dish> {
    private final DataSource dataSource;
    private final DishMapper dishMapper;
    private final IngredientCrudOperations ingredientCrudOperations;
    final PostgresNextValId nextValId = new PostgresNextValId();
    @Override
    public List<Dish> getAll(int page, int size) {
        List<Dish> dishes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select d.id, d.name, d.unit_price from dish s order by d.id asc limit ? offset ?")) {
            statement.setInt(1, size);
            statement.setInt(2, size * (page - 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Dish dish = dishMapper.apply(resultSet);
                    dishes.add(dish);
                }
                return dishes;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public Dish findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select d.id, d.name, d.unit_price from dish d where d.id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return dishMapper.apply(resultSet);
                }
                throw new NotFoundException("Dish.id=" + id + " not found");
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public List<Dish> saveAll(List<Dish> entities) {
        List<Dish> dishes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            entities.forEach(entityToSave -> {
                try (PreparedStatement statement =
                        connection.prepareStatement("insert into dish (id, name, unit_price) values (?, ?, ?)" +
                                " on conflict (id) do update set name.exclude, unit_price.exclude" +
                                " returning id, name, unit_price")) {
                    long id = entityToSave.getId() == null ? nextValId.nextID("dish", connection) : entityToSave.getId();
                    statement.setLong(1, id);
                    statement.setString(2, entityToSave.getName());
                    statement.setDouble(3, entityToSave.getUnitPrice());
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        Dish savedDish = dishMapper.apply(resultSet);
                        List<Ingredient> ingredients = ingredientCrudOperations.saveAll(entityToSave.getIngredients());
                        savedDish.setIngredients(ingredients);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException e) {
            throw new ServerException(e);
        }
        return dishes;
    }
}
