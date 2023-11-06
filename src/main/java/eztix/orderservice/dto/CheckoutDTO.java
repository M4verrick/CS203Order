package eztix.orderservice.dto;

import lombok.Data;

@Data
public class CheckoutDTO {

    ProductDTO[] products;

    private Long eventId;
    private Long purchaseRequestId;
    private String eventTitle;
    private String bannerURL;
    private String successUrl;
    private String failureUrl;
}
