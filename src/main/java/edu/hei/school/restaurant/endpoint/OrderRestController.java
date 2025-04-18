package edu.hei.school.restaurant.endpoint;

import edu.hei.school.restaurant.model.Order;
import edu.hei.school.restaurant.service.OrderService;
import edu.hei.school.restaurant.service.exception.ClientException;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import edu.hei.school.restaurant.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderRestController {
    private final OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<?> getAll(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "pageSize", required = false) Integer pageSize){
        try {
            return ResponseEntity.ok(orderService.findAll(page, pageSize));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/orders/{reference}")
    public ResponseEntity<?> searchOrders (
            @PathVariable String reference
    ) {
        try {
            List<Order> orders = orderService.findByReference(reference);
            return ResponseEntity.ok(orders);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
