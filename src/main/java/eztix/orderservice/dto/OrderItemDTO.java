package eztix.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderItemDTO {
    private String ticket_type;
    private Integer quantity;
    private Double price;
    private OffsetDateTime start_time;
    private OffsetDateTime end_time;
}
