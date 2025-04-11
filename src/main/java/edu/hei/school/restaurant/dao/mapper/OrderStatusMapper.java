package edu.hei.school.restaurant.dao.mapper;

import edu.hei.school.restaurant.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class OrderStatusMapper implements Function<ResultSet, OrderStatus> {

    @Override
    public OrderStatus apply(ResultSet resultSet) {
        return null;
    }
}
