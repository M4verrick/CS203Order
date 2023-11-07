package eztix.orderservice.service;

import com.stripe.model.LineItem;
import com.stripe.model.LineItemCollection;
import com.stripe.model.checkout.Session;
import eztix.orderservice.dto.*;
import eztix.orderservice.exception.RequestValidationException;
import eztix.orderservice.exception.ResourceNotFoundException;
import eztix.orderservice.model.PaymentOrder;
import eztix.orderservice.model.OrderItem;
import eztix.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public List<OrderListingDTO> getAllOrders(String customerId) {
        Stream<PaymentOrder> orderStream = orderRepository.findByCustomerIdOrderByPaymentDateTime(customerId);

        return orderStream.map(order -> {
            final OffsetDateTime[] latest = {OffsetDateTime.MIN};
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

    public PaymentOrder getOrderById(Long orderId){
            return orderRepository.findById(orderId).orElseThrow(() ->
                    new ResourceNotFoundException(String.format("order with id %d does not exist", orderId)));
    }

    public PaymentOrder getOrderByPurchaseRequestId(Long id) {
        return orderRepository.findByPurchaseRequestId(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("order with purchase request id %d does not exist", id)));
    }

    public void addNewOrder(LineItemCollection lineItems, String customerId,
                            Long eventId, Long purchaseRequestId, Long total, Session session){

        System.out.println("YAY");
        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Singapore"));

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<EventDTO> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/v1/event/1", EventDTO.class);
        EventDTO event = responseEntity.getBody();


        PaymentOrder paymentOrder = PaymentOrder.builder()
                .customerId(customerId)
                .eventId(eventId)
                .purchaseRequestId(purchaseRequestId)
                .paymentDateTime(now)
                .totalAmount(total/100)
                .eventName(event.getName())
                .eventCategory(event.getCategory())
                .eventArtist(event.getArtist())
                .eventBannerURL(event.getBannerURL())
                .eventSeatMapURL(event.getSeatMapURL())
                .eventLocation(event.getLocation())
                .eventStartTime(event.getStart_datetime())
                .eventEndTime(event.getEnd_datetime())
                .build();

        ArrayList<OrderItem> orderItems = new ArrayList<>();

        int i = 0;
        for (LineItem item: lineItems.getData()) {

            if (item.getDescription().equals("Facility Fee") || item.getDescription().equals("Service Fee")) {
                continue;
            }
            System.out.println(item);

            Long ticketId = Long.valueOf(session.getMetadata().get(String.format("ticketId%d", i)));

            ResponseEntity<TicketTypeDTO> responseEntityItem = restTemplate.getForEntity(String.format("http://localhost:8080/api/v1/ticket-type/%d/date", ticketId), TicketTypeDTO.class);
            TicketTypeDTO ticketType = responseEntityItem.getBody();

            System.out.println(ticketType);

            orderItems.add(OrderItem.builder()
                    .ticketType(ticketType.getTicketType())
                    .quantity(item.getQuantity())
                    .price(item.getPrice().getUnitAmount() / 100)
                    .startTime(ticketType.getStartDateTime())
                    .endTime(ticketType.getEndDateTime())
                    .paymentOrder(paymentOrder)
                    .build());
            i++;

        }
        paymentOrder.setOrderItems(orderItems);

        orderRepository.save(paymentOrder);


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
                    .ticketType(o.getTicket_type())
                    .quantity(o.getQuantity())
                    .price(o.getPrice())
                    .build();
            orderItems.add(newOrderItem);
        }
        return orderItems;

    }


}
