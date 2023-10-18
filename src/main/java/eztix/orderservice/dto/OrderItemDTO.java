package eztix.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private String ticket_type;
    private Integer quantity;
    private BigDecimal price;
}
