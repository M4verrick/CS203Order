package eztix.orderservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderListingDTO {
    private Long id;
    private String eventName;
    private String bannerURL;
    private String status;
}
