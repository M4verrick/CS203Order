package eztix.orderservice.dto;

import lombok.Data;


import java.time.OffsetDateTime;

@Data
public class TicketTypeDTO {

    String ticketType;
    OffsetDateTime startDateTime;
    OffsetDateTime endDateTime;


}

