package edu.hei.school.restaurant.endpoint.mapper;

import edu.hei.school.restaurant.dao.operations.DishCrudOperations;
import edu.hei.school.restaurant.endpoint.rest.*;
import edu.hei.school.restaurant.model.Dish;
import edu.hei.school.restaurant.model.DishIngredient;
import edu.hei.school.restaurant.model.Ingredient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DishRestMapper {
    private final IngredientBasicMapper ingredientBasicMapper;

    public DishRest toRest(Dish dish) {
        List<DishIngredientRest> ingredientRests = dish.getDishIngredients().stream()
                .map(this::toDishIngredientRest)
                .toList();

        return new DishRest(
                dish.getId(),
                dish.getName(),
                dish.getAvailableQuantity(),
                dish.getUnitPrice(),
                ingredientRests
        );
    }

    private DishIngredientRest toDishIngredientRest(DishIngredient dishIngredient) {
        Ingredient ingredient = dishIngredient.getIngredient();
        IngredientBasicProperty basic = ingredientBasicMapper.apply(dishIngredient);
        return new DishIngredientRest(
                basic,
                dishIngredient.getRequiredQuantity()
        );
    }
}
