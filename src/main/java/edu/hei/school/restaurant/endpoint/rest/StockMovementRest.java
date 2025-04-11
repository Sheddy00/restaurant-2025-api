package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.model.enums.StockMovementType;
import edu.hei.school.restaurant.model.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class StockMovementRest {
    private Long id;
    private Double quantity;
    private Unit unit;
    private StockMovementType type;
    private Instant creationDatetime;
}
