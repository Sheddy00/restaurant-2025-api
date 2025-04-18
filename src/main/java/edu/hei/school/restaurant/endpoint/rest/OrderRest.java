package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.model.Dish;
import edu.hei.school.restaurant.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class OrderRest {
    private Long id;
    private String reference;
    private List<DishOrderRest> dishes;
    private OrderStatus status;
}
