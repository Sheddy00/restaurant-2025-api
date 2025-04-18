package edu.hei.school.restaurant.dao.mapper;

import edu.hei.school.restaurant.dao.operations.OrderStatusCrudOperation;
import edu.hei.school.restaurant.model.Order;
import edu.hei.school.restaurant.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class OrderMapper implements Function<ResultSet, Order> {
    private final OrderStatusCrudOperation orderStatusCrudOperation;
    @SneakyThrows
    @Override
    public Order apply(ResultSet resultSet) {
        Long idOrder = resultSet.getLong("id");
        List<OrderStatus> orderStatuses = orderStatusCrudOperation.findByIdOrder(idOrder);

        Order order = new Order();
        order.setId(resultSet.getLong("id"));
        order.setReference(resultSet.getString("reference"));
        order.setCreatedAt(resultSet.getTimestamp("created_at").toInstant());
        order.setStatusHistory(orderStatuses);
        return order;
    }
}
