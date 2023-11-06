package eztix.orderservice.service;

import eztix.orderservice.dto.OrderDTO;
import eztix.orderservice.dto.OrderItemDTO;
import eztix.orderservice.dto.OrderListingDTO;
import eztix.orderservice.exception.RequestValidationException;
import eztix.orderservice.exception.ResourceNotFoundException;
import eztix.orderservice.model.PaymentOrder;
import eztix.orderservice.model.OrderItem;
import eztix.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class OrderService {
    // constructor injection to set the OrderRepository
    private final OrderRepository orderRepository;

    // Get all orders by customer ID
    @Transactional
    public List<OrderListingDTO> getAllOrders(String customerId) {
        // fetch orders from the repository
        Stream<PaymentOrder> orderStream = orderRepository.findByCustomerIdOrderByPaymentDateTime(customerId);

        return orderStream.map(order -> {
            final OffsetDateTime[] latest = {OffsetDateTime.MIN};
            // find the latest end time among order items
            order.getOrderItems().forEach((items) -> {
                if (items.getEndTime().isAfter(latest[0])){
                    latest[0] = items.getEndTime();
                }
            });

            return OrderListingDTO.builder()
                    .id(order.getId())
                    .eventName(order.getEventName())
                    .bannerURL(order.getEventBannerURL())
                    .status(latest[0].isAfter(OffsetDateTime.now()) ? "recurring" : "past")
                    .build();
        }).sorted(((o1, o2) -> o2.getStatus().compareTo(o1.getStatus()))).toList();

    }

    // retrieve PaymentOrder by ID else throw resource not found exception
    public PaymentOrder getOrderById(Long orderId){
            return orderRepository.findById(orderId).orElseThrow(() ->
                    new ResourceNotFoundException(String.format("order with id %d does not exist", orderId)));
    }

    public PaymentOrder addNewOrder(OrderDTO orderDTO){

        // check valid customer ID
        if (orderDTO.getCustomer_id() == null){
            throw new RequestValidationException("customer id cannot be null");
        }
        // check purchase request ID
        if (orderDTO.getPurchase_request_id() == null){
            throw new RequestValidationException("purchase request id cannot be null");
        }
        // create a new PaymentOrder from OrderDTO
        PaymentOrder order = PaymentOrder.builder().
                customerId(orderDTO.getCustomer_id())
                .purchaseRequestId(orderDTO.getPurchase_request_id())
                .eventId(orderDTO.getEvent_id())
                .paymentDateTime(orderDTO.getPayment_date_time())
                .totalAmount(orderDTO.getTotal_amount())
                .eventName(orderDTO.getEvent_name())
                .eventCategory(orderDTO.getEvent_category())
                .eventArtist(orderDTO.getEvent_artist())
                .eventBannerURL(orderDTO.getEvent_banner_url())
                .eventSeatMapURL(orderDTO.getEvent_seat_map_url())
                .eventLocation(orderDTO.getEvent_location())
                .build();

        // process and set OrderItems
        List<OrderItem> orderItems = processOrderItem(orderDTO.getOrderItems());
        order.setOrderItems(orderItems);
        return orderRepository.save(order);
    }



    // delete order by ID; if invalid ID throws request validation exception
    public void deleteById(Long id){
        if (id == null){
            throw new RequestValidationException("order id cannot be null");
        }
        orderRepository.deleteById(id);
    }

    // given a list of orderItemDTO, return a list of OrderItem objects
    public List<OrderItem> processOrderItem(List<OrderItemDTO> orderItemDTO){
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO o : orderItemDTO){
            // create and add OrderItem objects from OrderItemDTO
            OrderItem newOrderItem = OrderItem.builder()
                    .ticketType(o.getTicket_type())
                    .quantity(o.getQuantity())
                    .price(o.getPrice())
                    .build();
            orderItems.add(newOrderItem);
        }
        return orderItems;

    }


}
