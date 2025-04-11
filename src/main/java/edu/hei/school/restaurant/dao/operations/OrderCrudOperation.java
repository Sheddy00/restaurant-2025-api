package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.model.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderCrudOperation implements CrudOperations<Order> {
    @Override
    public List<Order> getAll(int page, int size) {
        return List.of();
    }

    @Override
    public Order findById(Long id) {
        return null;
    }

    @Override
    public List<Order> saveAll(List<Order> entities) {
        return List.of();
    }
}
