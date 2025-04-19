package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.PostgresNextValId;
import edu.hei.school.restaurant.dao.mapper.IngredientMapper;
import edu.hei.school.restaurant.dao.mapper.OrderMapper;
import edu.hei.school.restaurant.model.*;
import edu.hei.school.restaurant.model.enums.StatusEnum;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import edu.hei.school.restaurant.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderCrudOperation implements CrudOperations<Order> {
    private final DataSource dataSource;
    private final OrderMapper orderMapper;
    private final DishOrderOperations dishOrderOperations;
    final PostgresNextValId nextValId = new PostgresNextValId();

    @Override
    public List<Order> getAll(int page, int size) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select os.id, os.reference, os.created_at from \"orders\" os limit ? offset ?")) {
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
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select os.id, os.reference, os.created_at, os.status from \"orders\" os where os.id = ?"
             )) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return orderMapper.apply(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    public Order save(Order order) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     """
                     insert into \"orders\" (id, reference, created_at, status) values (?, ?, ?, ?)
                     on conflict (id) do update set reference = excluded.reference,
                         created_at = excluded.created_at,
                         status = excluded.status
                     returning id, reference, created_at, status
                     """
             )) {

            Long id = (order.getId() == null) ? nextValId.nextID("orders", connection) : order.getId();

            statement.setLong(1, id);
            statement.setString(2, order.getReference());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(String.valueOf(order.getCreatedAt())));
            statement.setString(4, order.getActualStatus().name());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return orderMapper.apply(resultSet);
                } else {
                    throw new ServerException("Failed to save order");
                }
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @SneakyThrows
    @Override
    public List<Order> saveAll(List<Order> entities) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
             entities.forEach(entityToSave -> {
                 try (PreparedStatement statement = connection.prepareStatement(
                         """
                          insert into \"orders\" (id, reference, created_at, status) values (?, ?, ?, ?)
                          on conflict (id) do update set reference = excluded.reference,
                              created_at = excluded.created_at,
                              status = excluded.status
                          returning id, reference, created_at, status
                          """
                 )) {
                     Long id = (entityToSave.getId() == null) ? nextValId.nextID("orders", connection) : entityToSave.getId();
                     statement.setLong(1, id);
                     statement.setString(2, entityToSave.getReference());
                     statement.setTimestamp(3, Timestamp.valueOf(String.valueOf(entityToSave.getCreatedAt())));
                     statement.setString(4, entityToSave.getActualStatus().name());
                     ResultSet resultSet = statement.executeQuery();

                     if (resultSet.next()) {
                         Order orderToSave = orderMapper.apply(resultSet);
                         List<DishOrder> dishOrders = dishOrderOperations.saveAll(entityToSave.getDishOrders());
                         orderToSave.setDishOrders(dishOrders);
                         orders.add(orderMapper.apply(resultSet));
                     }
                 } catch (SQLException e) {
                     throw new ServerException(e);
                 }
             });
             return orders;
        }
    }

    public List<Order> getByReference(String reference) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select os.id, os.reference, os.created_at from \"orders\" os where os.reference like ?"
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

    public void updateDishStatus(String orderReference, Long dishId, StatusEnum newStatus) {
        try (Connection connection = dataSource.getConnection()) {
            Long dishOrderId = null;

            try (PreparedStatement ps = connection.prepareStatement(
                    """
                        select do.id
                        from orders o
                        join dish_order do on o.id = do.id_order
                        where o.reference = ? and do.id_dish = ?
                    """
            )) {
                ps.setString(1, orderReference);
                ps.setLong(2, dishId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        dishOrderId = rs.getLong("id");
                    } else {
                        throw new NotFoundException("Dish with ID " + dishId + " not found in order " + orderReference);
                    }
                }
            }

            try (PreparedStatement ps = connection.prepareStatement(
                    """
                            insert into dish_order_status (id_dish_order, status, created_at)
                            values (?, ?, ?)
                        """
            )) {
                ps.setLong(1, dishOrderId);
                ps.setString(2, newStatus.name());
                ps.setTimestamp(3, Timestamp.from(Instant.now()));
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new ServerException("Failed to update dish status");
        }
    }

}
