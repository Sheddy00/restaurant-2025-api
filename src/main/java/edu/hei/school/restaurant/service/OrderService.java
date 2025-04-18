package edu.hei.school.restaurant.service;

import edu.hei.school.restaurant.dao.operations.OrderCrudOperation;
import edu.hei.school.restaurant.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderCrudOperation orderCrudOperation;
    public List<Order> findAll(int page, int size) {
        return orderCrudOperation.getAll(page, size);
    }

    public List<Order> findByReference(String reference) {
        return orderCrudOperation.getByReference(reference);
    }
}
