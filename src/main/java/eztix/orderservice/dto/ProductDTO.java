package eztix.orderservice.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private String dateTime;
    private String ticketType;
    //private String currency;

    private long unitPrice;
    private long quantity;

}
