package edu.hei.school.restaurant.dao.mapper;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.model.Dish;
import edu.hei.school.restaurant.model.DishOrder;
import edu.hei.school.restaurant.model.enums.StatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishOrderMapper implements Function<ResultSet, DishOrder> {
    private final DataSource dataSource;
    @SneakyThrows
    @Override
    public DishOrder apply(ResultSet resultSet) {
        DishOrder dishOrder = new DishOrder();
        dishOrder.setId(resultSet.getLong("id"));
        dishOrder.setOrderId(resultSet.getLong("id_order"));

        Dish dish = new Dish();
        dish.setId(resultSet.getLong("id_dish"));
        dishOrder.setDish(dish);

        dishOrder.setQuantityToOrder(resultSet.getDouble("quantity_to_order"));

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(
                     "select status, created_at from dish_order_status where dish_order_id = ?")) {
            stmt.setLong(1, dishOrder.getId());
            try (var rsStatus = stmt.executeQuery()) {
                while (rsStatus.next()) {
                    dishOrder.addStatus(
                            StatusEnum.valueOf(rsStatus.getString("status")),
                            rsStatus.getTimestamp("created_at").toInstant()
                    );
                }
            }
        }
        return dishOrder;
    }
}
