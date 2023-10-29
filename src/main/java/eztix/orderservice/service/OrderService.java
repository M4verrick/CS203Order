package eztix.orderservice.service;

import eztix.orderservice.dto.OrderDTO;
import eztix.orderservice.dto.OrderItemDTO;
import eztix.orderservice.exception.RequestValidationException;
import eztix.orderservice.exception.ResourceNotFoundException;
import eztix.orderservice.model.OrderItem;
import eztix.orderservice.model.Orders;
import eztix.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public Orders getOrderById(Long orderId){
            return orderRepository.findById(orderId).orElseThrow(() ->
                    new ResourceNotFoundException(String.format("order with id %d does not exist", orderId)));
    }

    public Orders addNewOrder(OrderDTO orderDTO){
        // check valid customer ID
        if (orderDTO.getCustomer_id() == null){
            throw new RequestValidationException("customer id cannot be null");
        }
        // check purchase request ID
        if (orderDTO.getPurchase_request_id() == null){
            throw new RequestValidationException("purchase request id cannot be null");
        }

        Orders order = Orders.builder().
                customer_id(orderDTO.getCustomer_id())
                .purchase_request_id(orderDTO.getPurchase_request_id())
                .event_id(orderDTO.getEvent_id())
                .payment_date_time(orderDTO.getPayment_date_time())
                .total_amount(orderDTO.getTotal_amount())
                .event_name(orderDTO.getEvent_name())
                .event_category(orderDTO.getEvent_category())
                .event_artist(orderDTO.getEvent_artist())
                .event_banner_url(orderDTO.getEvent_banner_url())
                .event_seatmap_url(orderDTO.getEvent_seatmap_url())
                .event_location(orderDTO.getEvent_location())
                .build();

        List<OrderItem> orderItems = processOrderItem(orderDTO.getOrderItems());
        order.setOrderItems(orderItems);
        return orderRepository.save(order);
    }

    public void deleteById(Long id){
        if (id == null){
            throw new RequestValidationException("order id cannot be null");
        }
        orderRepository.deleteById(id);
    }

    public List<OrderItem> processOrderItem(List<OrderItemDTO> orderItemDTO){
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO o : orderItemDTO){
            OrderItem newOrderItem = OrderItem.builder()
                    .ticket_type(o.getTicket_type())
                    .quantity(o.getQuantity())
                    .price(o.getPrice())
                    .build();
            orderItems.add(newOrderItem);
        }
        return orderItems;

    }


}
