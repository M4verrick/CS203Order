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

    /**
     * Retrieve a list of OrderListingDTO.
     *
     * @param authentication Used to authenticate the user.
     * @return List of OrderListingDTO at the specified URL.
     */
    @GetMapping("/api/v1/order")
    public ResponseEntity<List<OrderListingDTO>> getAllOrders(Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK).
                body(orderService.getAllOrders(authentication.getName()));
    }

    /**
     * Retrieve a PaymentOrder based on the ID.
     *
     * @param orderId A Long value representing the order ID.
     * @return PaymentOrder at the specified URL.
     */
    @GetMapping("/api/v1/order/{orderId}")
    public ResponseEntity<PaymentOrder> getOrderById(@PathVariable Long orderId){
        return ResponseEntity.status(HttpStatus.OK).
                body(orderService.getOrderById(orderId));
    }

    /**
     * Add a new PaymentOrder.
     *
     * @param orderDTO DTO containing information needed to create a new Payment Order.
     * @return PaymentOrder that has been successfully added.
     */
    @PostMapping("api/v1/order")
    public ResponseEntity<PaymentOrder> addOrder(@RequestBody OrderDTO orderDTO){
        return ResponseEntity.status(HttpStatus.OK).
                body(orderService.addNewOrder(orderDTO));
    }

    /**
     * Delete a PaymentOrder.
     *
     * @param orderId A Long value representing the order ID to be deleted.
     * @return String specifying the successful deletion of the PaymentOrder.
     */
    @DeleteMapping("api/v1/order/{orderId}")
    public ResponseEntity<String> deleteOrderById(@PathVariable Long orderId){
        orderService.deleteById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(String.format("order with order id %d has been deleted", orderId));
    }
}
