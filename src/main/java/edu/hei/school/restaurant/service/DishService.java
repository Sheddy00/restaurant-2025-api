package edu.hei.school.restaurant.service;

import edu.hei.school.restaurant.dao.operations.DishCrudOperations;
import edu.hei.school.restaurant.dao.operations.IngredientCrudOperations;
import edu.hei.school.restaurant.endpoint.rest.CreateDishIngredient;
import edu.hei.school.restaurant.model.Dish;
import edu.hei.school.restaurant.model.DishIngredient;
import edu.hei.school.restaurant.model.Ingredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishCrudOperations dishCrudOperations;
    private final IngredientCrudOperations ingredientCrudOperations;

    public List<Dish> getAllDishes(int page, int size) {
        return dishCrudOperations.getAll(page, size);
    }

    public Dish getDishById(Long id) {
        return dishCrudOperations.findById(id);
    }

    public List<Dish> saveAllDishes(List<Dish> dishes) {
        return dishCrudOperations.saveAll(dishes);
    }

    public Dish saveSingleDish(Dish dish) {
        return saveAllDishes(List.of(dish)).get(0);
    }

    public Dish updateDishIngredients(Long dishId, List<CreateDishIngredient> dishIngredients) {
        Dish dish = dishCrudOperations.findById(dishId);

        List<DishIngredient> newDishIngredients = dishIngredients.stream()
                .map(di -> {
                    Ingredient ingredient = ingredientCrudOperations.findById(di.getId());
                    return new DishIngredient(ingredient, di.getRequiredQuantity());
                })
                .toList();

        dish.setDishIngredients(newDishIngredients);
        return dishCrudOperations.saveAll(List.of(dish)).get(0);
    }



}
