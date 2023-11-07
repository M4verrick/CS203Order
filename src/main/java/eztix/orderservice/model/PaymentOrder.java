package eztix.orderservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "payment_order")
@Table(name = "PAYMENT_ORDER" )
public class PaymentOrder {

    @Id
    @SequenceGenerator(name = "payment_order_sequence", sequenceName = "payment_order_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_order_sequence")
    @Schema(hidden = true)
    private Long id;

    @NotNull
    @Column(name = "customer_id")
    private String customerId;

    @NotNull
    @Column(name = "purchase_request_id")
    private Long purchaseRequestId;

    @NotNull
    @Column(name = "event_id")
    private Long eventId;

    @NotNull
    @Column(name = "payment_date_time")
    private OffsetDateTime paymentDateTime;

    @NotNull
    @Column(name = "total_amount")
    private Long totalAmount;

    @NotNull
    @Column(name = "event_name")
    private String eventName;

    @NotNull
    @Column(name = "event_category")
    private String eventCategory;

    @NotNull
    @Column(name = "event_artist")
    private String eventArtist;

    @NotNull
    @Column(name = "event_banner_url")
    private String eventBannerURL;

    @NotNull
    @Column(name = "event_seat_map_url")
    private String eventSeatMapURL;

    @NotNull
    @Column(name = "event_location")
    private String eventLocation;

    @NotNull
    @Column(name = "event_start_time")
    private OffsetDateTime eventStartTime;

    @NotNull
    @Column(name = "event_end_time")
    private OffsetDateTime eventEndTime;

    @JsonManagedReference
    @OneToMany(mappedBy = "paymentOrder",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

}