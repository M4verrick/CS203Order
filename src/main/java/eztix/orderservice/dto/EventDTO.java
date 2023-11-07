package eztix.orderservice.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class EventDTO {

    private Long id;

    private String name;

    private String category;

    private String artist;

    private String description;

    private String bannerURL;

    private String seatMapURL;

    private String location;

    private Boolean isFeatured;

    private Integer featureSequence;

    private OffsetDateTime start_datetime;

    private OffsetDateTime end_datetime;



}
