package edu.hei.school.restaurant.model;

import edu.hei.school.restaurant.model.enums.OrderPaymentStatus;
import edu.hei.school.restaurant.model.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishOrder {
    private Long id;
    private Long orderId;
    private Dish dish;
    private Double quantityToOrder;
    private List<DishOrderStatus> statusHistory = new ArrayList<>();

    public DishOrder(Dish dish, Double quantityToOrder) {
        this.dish = dish;
        this.quantityToOrder = quantityToOrder;
    }

    public void addStatus(StatusEnum status, Instant dateTime) {
        statusHistory.add(new DishOrderStatus(status, dateTime));
    }

    public StatusEnum getActualStatus() {
        return statusHistory.stream()
                .max(Comparator.comparing(DishOrderStatus::getCreatedAt))
                .map(DishOrderStatus::getStatus)
                .orElse(null);
    }
}
