package eztix.orderservice.controller;

import eztix.orderservice.dto.OrderDTO;

import eztix.orderservice.dto.OrderListingDTO;
import eztix.orderservice.model.PaymentOrder;
import eztix.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/api/v1/order")
    public ResponseEntity<List<OrderListingDTO>> getAllOrders(Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK).
                body(orderService.getAllOrders(authentication.getName()));
    }

    // Get Order by id
    @GetMapping("/api/v1/order/{orderId}")
    public ResponseEntity<PaymentOrder> getOrderById(@PathVariable Long orderId){
        return ResponseEntity.status(HttpStatus.OK).
                body(orderService.getOrderById(orderId));
    }

    // Add a new Order
    @PostMapping("api/v1/order")
    public ResponseEntity<PaymentOrder> addOrder(@RequestBody OrderDTO orderDTO){
        return ResponseEntity.status(HttpStatus.OK).
                body(orderService.addNewOrder(orderDTO));
    }

    // Delete an order
    @DeleteMapping("api/v1/order/{orderId}")
    public ResponseEntity<String> deleteOrderById(@PathVariable Long orderId){
        orderService.deleteById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(String.format("order with order id %d has been deleted", orderId));
    }


}
