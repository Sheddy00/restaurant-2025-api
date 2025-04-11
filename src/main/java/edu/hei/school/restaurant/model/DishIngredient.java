package edu.hei.school.restaurant.model;

import edu.hei.school.restaurant.model.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishIngredient {
    private Long id;
    private Ingredient ingredient;
    private Double requiredQuantity;
    private Unit unit;

    public DishIngredient(Ingredient ingredient, Double requiredQuantity) {
        this.ingredient = ingredient;
        this.requiredQuantity = requiredQuantity;
    }
}
