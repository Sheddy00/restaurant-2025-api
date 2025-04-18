package edu.hei.school.restaurant.endpoint.mapper;

import edu.hei.school.restaurant.endpoint.rest.DishOrderRest;
import edu.hei.school.restaurant.endpoint.rest.OrderRest;
import edu.hei.school.restaurant.model.DishOrder;
import edu.hei.school.restaurant.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderRestMapper {
    public OrderRest toRest(Order order) {
        //List<DishOrderRest> dishes = order.getDishOrders().stream()
               // .map(this::toRest)
             return null;
    }

    private DishOrderRest toDishOrderRest(DishOrder dishOrder) {
        //Order order = dishOrder.getDish()
        return null;
    }
}
