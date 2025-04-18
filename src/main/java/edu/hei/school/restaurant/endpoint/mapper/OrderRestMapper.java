package edu.hei.school.restaurant.endpoint.mapper;

import edu.hei.school.restaurant.endpoint.rest.DishOrderRest;
import edu.hei.school.restaurant.endpoint.rest.OrderRest;
import edu.hei.school.restaurant.model.DishOrder;
import edu.hei.school.restaurant.model.Order;
import edu.hei.school.restaurant.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderRestMapper {
    public OrderRest toRest(Order order) {
        return new OrderRest(
                order.getId(),
                order.getReference(),
                toDishOrderRestList(order.getDishOrders()),
                getLastStatus(order.getStatusHistory())
        );
    }

    private List<DishOrderRest> toDishOrderRestList(List<DishOrder> dishOrders) {
        return dishOrders.stream()
                .map(this::toDishOrderRest)
                .collect(Collectors.toList());
    }

    private DishOrderRest toDishOrderRest(DishOrder dishOrder) {
        return new DishOrderRest(
                dishOrder.getDish().getId(),
                dishOrder.getDish().getName(),
                dishOrder.getDish().getUnitPrice(),
                dishOrder.getQuantityToOrder(),
                dishOrder.getStatusHistory().getFirst()
        );
    }

    private OrderStatus getLastStatus(List<OrderStatus> history) {
        return history.stream()
                .max((a, b) -> a.getChangedAt().compareTo(b.getChangedAt()))
                .orElse(null);
    }
}
