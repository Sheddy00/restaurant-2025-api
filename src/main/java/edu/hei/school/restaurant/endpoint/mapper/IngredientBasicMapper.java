package edu.hei.school.restaurant.endpoint.mapper;

import edu.hei.school.restaurant.endpoint.rest.IngredientBasicProperty;
import edu.hei.school.restaurant.model.DishIngredient;
import edu.hei.school.restaurant.model.Ingredient;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class IngredientBasicMapper implements Function<DishIngredient, IngredientBasicProperty> {

    @Override
    public IngredientBasicProperty apply(DishIngredient ingredient) {
        return new IngredientBasicProperty(ingredient.getId(), ingredient.getIngredient().getName());
    }
}
