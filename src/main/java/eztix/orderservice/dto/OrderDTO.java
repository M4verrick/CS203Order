package eztix.orderservice.dto;

import eztix.orderservice.model.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Data
public class OrderDTO {
    private Long customer_id;
    private Long purchase_request_id;
    private Long event_id;
    private OffsetDateTime payment_date_time;
    private BigDecimal total_amount;
    private String event_name;
    private String event_category;
    private String event_artist;
    private String event_banner_url;
    private String event_seatmap_url;
    private String event_location;
    private List<OrderItemDTO> orderItems;
}
