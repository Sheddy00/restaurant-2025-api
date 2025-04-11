package edu.hei.school.restaurant.model;

import edu.hei.school.restaurant.model.enums.OrderPaymentStatus;
import edu.hei.school.restaurant.model.enums.StatusEnum;
import edu.hei.school.restaurant.model.enums.TableNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private Long id;
    private String reference;
    private Instant createdAt;
    private List<DishOrder> dishOrders = new ArrayList<>();
    private List<OrderStatus> statusHistory = new ArrayList<>();

    public void addOrUpdateDish(Dish dish, Double quantity) {
        Optional<DishOrder> existing = dishOrders.stream()
                .filter(do_ -> do_.getDish().getId().equals(dish.getId()))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantityToOrder(quantity);
        } else {
            dishOrders.add(new DishOrder(dish, quantity));
        }
    }

    public StatusEnum getActualStatus() {
        return statusHistory.stream()
                .max(Comparator.comparing(OrderStatus::getChangedAt))
                .map(OrderStatus::getStatus)
                .orElse(null);
    }

    public void addStatus(StatusEnum status, Instant dateTime) {
        statusHistory.add(new OrderStatus(status, dateTime));
    }
}
