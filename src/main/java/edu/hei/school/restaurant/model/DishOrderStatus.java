package edu.hei.school.restaurant.model;

import edu.hei.school.restaurant.model.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class DishOrderStatus {
    private StatusEnum status;
    private Instant createdAt;
}
