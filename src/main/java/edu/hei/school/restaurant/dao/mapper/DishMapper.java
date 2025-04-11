package edu.hei.school.restaurant.dao.mapper;

import edu.hei.school.restaurant.model.Dish;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishMapper implements Function<ResultSet, Dish> {
    @SneakyThrows
    @Override
    public Dish apply(ResultSet resultSet) {
        Dish dish = new Dish();
        dish.setId(resultSet.getLong("id"));
        dish.setName(resultSet.getString("name"));
        dish.setUnitPrice(resultSet.getDouble("unit_price"));
        return dish;
    }
}
