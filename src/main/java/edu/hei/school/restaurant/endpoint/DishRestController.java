package edu.hei.school.restaurant.endpoint;

import edu.hei.school.restaurant.endpoint.mapper.DishRestMapper;
import edu.hei.school.restaurant.endpoint.rest.CreateDishIngredient;
import edu.hei.school.restaurant.endpoint.rest.DishRest;
import edu.hei.school.restaurant.endpoint.rest.IngredientRest;
import edu.hei.school.restaurant.model.Dish;
import edu.hei.school.restaurant.model.DishIngredient;
import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.model.Price;
import edu.hei.school.restaurant.service.DishService;
import edu.hei.school.restaurant.service.exception.ClientException;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import edu.hei.school.restaurant.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DishRestController {
    //private final DishRest dishRest;
    private final DishRestMapper dishRestMapper;
    private final DishService dishService;

    @GetMapping("/dishes")
    public ResponseEntity<?> getDishes(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "pageSize", required = false) Integer pageSize) {

        try {
            List<Dish> dishes = dishService.getAllDishes(page, pageSize);
            List<DishRest> dishRests = dishes.stream()
                    .map(dishRestMapper::toRest)
                    .toList();
            return ResponseEntity.ok().body(dishRests);
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/dishes/{id}/ingredients")
    public ResponseEntity<Object> updateDishIngredientQuantity(@PathVariable Long dishId, @RequestBody List<CreateDishIngredient> dishIngredients) {
        try {
            Dish updatedDish = dishService.updateDishIngredients(dishId, dishIngredients);
            DishRest updatedDishRest = dishRestMapper.toRest(updatedDish);
            return ResponseEntity.ok(updatedDishRest);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
