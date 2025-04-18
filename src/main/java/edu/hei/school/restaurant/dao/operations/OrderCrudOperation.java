package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.PostgresNextValId;
import edu.hei.school.restaurant.dao.mapper.OrderMapper;
import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.model.Order;
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
public class OrderCrudOperation implements CrudOperations<Order> {
    private final DataSource dataSource;
    private final OrderMapper orderMapper;
    final PostgresNextValId nextValId = new PostgresNextValId();

    @Override
    public List<Order> getAll(int page, int size) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select os.id, os.reference, os.created_at from \"orders\" os /*order by os.id asc*/ limit ? offset ?")) {
            statement.setInt(1, size);
            statement.setInt(2, size * (page - 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = orderMapper.apply(resultSet);
                    orders.add(order);
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public Order findById(Long id) {
        return null;
    }

    @Override
    public List<Order> saveAll(List<Order> entities) {
        throw new RuntimeException("Not implemented yet");
    }

    public List<Order> getByReference(String reference) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select o.id, o.reference, o.created_at from orders o where o.reference like ?"
        )) {
            statement.setString(1, "%" +reference+ "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = orderMapper.apply(resultSet);
                    orders.add(order);
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }
}
