package edu.hei.school.restaurant.dao.mapper;

import edu.hei.school.restaurant.model.OrderStatus;
import edu.hei.school.restaurant.model.enums.StatusEnum;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class OrderStatusMapper implements Function<ResultSet, OrderStatus> {

    @SneakyThrows
    @Override
    public OrderStatus apply(ResultSet resultSet) {
        OrderStatus status = new OrderStatus();
        status.setStatus(StatusEnum.valueOf(resultSet.getString("status")));
        status.setChangedAt(resultSet.getTimestamp("created_at").toInstant());
        return status;
    }
}
