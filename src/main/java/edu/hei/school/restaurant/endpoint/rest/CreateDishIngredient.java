package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.model.Price;
import edu.hei.school.restaurant.model.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class CreateDishIngredient {
    private Long id;
    private String name;
    private Unit unit;
    private PriceRest price;
    private StockMovementRest stockMovement;
    private Instant dateValue;
    private Double requiredQuantity;
}
