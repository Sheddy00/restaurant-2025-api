package edu.hei.school.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishAvailability {
    private String name;
    private Double availability;

    public List<DishAvailability> getAllDishAvailability(int page, int size) {
        //DishAvailabilityOperation dao = new DishAvailabilityOperation();
        //return dao.findAll(page, size);
        throw new UnsupportedOperationException("Not supported yet for getAllDishAvailability.");
    }
}
