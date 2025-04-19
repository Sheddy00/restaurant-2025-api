package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.PostgresNextValId;
import edu.hei.school.restaurant.dao.mapper.DishOrderMapper;
import edu.hei.school.restaurant.model.DishOrder;
import edu.hei.school.restaurant.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishOrderOperations implements CrudOperations<DishOrder> {
    private final DataSource dataSource;
    private final DishOrderMapper dishOrderMapper;
    final PostgresNextValId nextValId = new PostgresNextValId();

    @Override
    public List<DishOrder> getAll(int page, int size) {
        List<DishOrder> dishOrders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select id, id_order, id_dish, quantity_to_order, current_status from dish_order order by id asc limit ? offset ?")) {
            statement.setInt(1, size);
            statement.setInt(2, size * (page - 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DishOrder dishOrder = dishOrderMapper.apply(resultSet);
                    dishOrders.add(dishOrder);
                }
                return dishOrders;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public DishOrder findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select id, id_order, id_dish, quantity_to_order, current_status from dish_order where id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return dishOrderMapper.apply(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public List<DishOrder> saveAll(List<DishOrder> entities) {
        return List.of();
    }
}
