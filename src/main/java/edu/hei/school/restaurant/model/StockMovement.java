package edu.hei.school.restaurant.model;

import edu.hei.school.restaurant.model.enums.StockMovementType;
import edu.hei.school.restaurant.model.enums.Unit;
import lombok.*;

import java.time.Instant;

@ToString(exclude = "ingredient")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StockMovement {
    private Long id;
    private Ingredient ingredient;
    private Double quantity;
    private Unit unit;
    private StockMovementType movementType;
    private Instant creationDatetime;
}
