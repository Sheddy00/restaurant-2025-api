package edu.hei.school.restaurant.model;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dish {
    private Long id;
    private String name;
    private Double unitPrice = 0.0;
    private List<Ingredient> ingredients;
    private List<DishIngredient> dishIngredients;
    private List<Order> orders;

    public double getTotalIngredientCount() {
        return dishIngredients.stream()
                .map(dishIgredient -> {
                    double actualPrice = dishIgredient.getIngredient().getActualPrice();
                    double quantity = dishIgredient.getRequiredQuantity();
                    return actualPrice * quantity;
                }).reduce(0.0, Double::sum);
    }

    public double getTotalIngredientCost(LocalDate date, List<DishIngredient> dishIngredients) {
        double totalCost = 0;
        for (DishIngredient dishIngredient : dishIngredients) {
            Ingredient ingredient = dishIngredient.getIngredient();
            Price priceAtDate = ingredient.getPrices().stream()
                    .filter(price -> price.getDateValue().isBefore(date) || price.getDateValue().isEqual(date))
                    .max(Comparator.comparing(Price::getDateValue))
                    .orElseThrow(() -> new IllegalArgumentException());

            totalCost += priceAtDate.getAmount() * dishIngredient.getRequiredQuantity();
        }
        return totalCost;
    }

    public double getGrossMargin() {
        return getUnitPrice() - getTotalIngredientCost(LocalDate.now(), dishIngredients);
    }

    public double getGrossMargin(LocalDate date) {
        double ingredientCost = getTotalIngredientCost(date, dishIngredients);
        return ((unitPrice - ingredientCost) / unitPrice * 100);
    }

    public Double getAvailableQuantity() {
        List<Double> allQuantitiesPossible = new ArrayList<>();
        for (DishIngredient dishIngredient : dishIngredients) {
            Ingredient ingredient = dishIngredient.getIngredient();
            double quantityPossibleForThatIngredient = ingredient.getAvailableQuantity() / dishIngredient.getRequiredQuantity();
            double roundedQuantityPossible = Math.ceil(quantityPossibleForThatIngredient);
            allQuantitiesPossible.add(roundedQuantityPossible);
        }
        return allQuantitiesPossible.stream().min(Double::compare).orElse(0.0);
    }

    public Double getAvailableQuantityAtADate(Instant datetime) {
        List<Double> allQuantitiesPossible = new ArrayList<>();
        for (DishIngredient dishIngredient : dishIngredients) {
            Ingredient ingredient = dishIngredient.getIngredient();
            double quantityPossibleForThatIngredient = ingredient.getAvailableQuantityAt(datetime) / dishIngredient.getRequiredQuantity();
            double roundedQuantityPossible = Math.ceil(quantityPossibleForThatIngredient);
            allQuantitiesPossible.add(roundedQuantityPossible);
        }
        return allQuantitiesPossible.stream().min(Double::compare).orElse(0.0);
    }
}
