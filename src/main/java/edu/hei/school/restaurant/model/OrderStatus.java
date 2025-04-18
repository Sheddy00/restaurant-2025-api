package edu.hei.school.restaurant.model;

import edu.hei.school.restaurant.model.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderStatus {
    private StatusEnum status;
    private Instant changedAt;
}
