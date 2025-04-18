package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.PostgresNextValId;
import edu.hei.school.restaurant.dao.mapper.OrderStatusMapper;
import edu.hei.school.restaurant.dao.mapper.PriceMapper;
import edu.hei.school.restaurant.model.OrderStatus;
import edu.hei.school.restaurant.model.Price;
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
public class OrderStatusCrudOperation implements CrudOperations<OrderStatus> {
    @Autowired
    private DataSource dataSource;
    private final OrderStatusMapper orderStatusMapper;
    final PostgresNextValId nextValId = new PostgresNextValId();

    @Override
    public List<OrderStatus> getAll(int page, int size) {
        return List.of();
    }

    @Override
    public OrderStatus findById(Long id) {
        return null;
    }

    @Override
    public List<OrderStatus> saveAll(List<OrderStatus> entities) {
        return List.of();
    }

    public List<OrderStatus> findByIdOrder(Long idOrder) {
        List<OrderStatus> orderStatuses = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select os.id, os.status, os.created_at from orderr_status os"
                     + " left join order o on os.id_order = o.id"
                     + " where os.id_order = ?")) {
            statement.setLong(1, idOrder);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    OrderStatus status = orderStatusMapper.apply(resultSet);
                    orderStatuses.add(status);
                }
                return orderStatuses;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }
}
