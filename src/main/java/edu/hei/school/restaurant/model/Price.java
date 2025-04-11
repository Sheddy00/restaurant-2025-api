package edu.hei.school.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;

import static java.time.LocalDate.now;

@ToString(exclude = "ingredient")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
public class Price {
    private Long id;
    private Ingredient ingredient;
    private Double amount;
    private LocalDate dateValue;

    public Price(Double amount) {
        this.amount = amount;
        this.dateValue = now();
    }

    public Price(Double amount, LocalDate dateValue) {
        this.amount = amount;
        this.dateValue = dateValue;
    }
}
