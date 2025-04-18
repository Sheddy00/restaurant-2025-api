package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.model.DishOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DishOrderRest {
    private Long id;
    private String name;
    private Double unitPrice;
    private Double requiredQuantity;
    private DishOrderStatus status;
}
