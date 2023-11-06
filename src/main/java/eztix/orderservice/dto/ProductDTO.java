package eztix.orderservice.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private Long ticketId;
    private String dateTime;
    private String ticketType;
    private long unitPrice;
    private long quantity;

}
