package eztix.orderservice.controller;

import eztix.orderservice.dto.OrderDTO;

import eztix.orderservice.model.Orders;
import eztix.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    // Get Order by id
    @GetMapping("/api/v1/order/{orderId}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long orderId){
        return ResponseEntity.status(HttpStatus.OK).
                body(orderService.getOrderById(orderId));
    }
    // Add a new Order
    @PostMapping("api/v1/order")
    public ResponseEntity<Orders> addOrder(@RequestBody OrderDTO orderDTO){
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
