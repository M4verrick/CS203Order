package eztix.orderservice.dto;

import lombok.Data;

@Data
public class CheckoutDTO {

    ProductDTO[] products;

    private String eventTitle;
    private String bannerURL;
    //private String successUrl;
    //private String cancelUrl;
}
