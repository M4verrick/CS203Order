package eztix.orderservice.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity (name = "orders")
@Table(name = "ORDERS" )
public class Orders {

    @Id
    @SequenceGenerator(name = "orders_sequence", sequenceName = "orders_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_sequence")
    @Schema(hidden = true)
    private Long id;

    @NotNull
    @Column(name = "customer_id")
    private Long customer_id;

    @NotNull
    @Column(name = "purchase_request_id")
    private Long purchase_request_id;

    @NotNull
    @Column(name = "event_id")
    private Long event_id;

    @NotNull
    @Column(name = "payment_date_time")
    private OffsetDateTime payment_date_time;

    @NotNull
    @Column(name = "total_amount")
    private BigDecimal total_amount;

    @NotNull
    @Column(name = "event_name")
    private String event_name;

    @NotNull
    @Column(name = "event_category")
    private String event_category;

    @NotNull
    @Column(name = "event_artist")
    private String event_artist;

    @NotNull
    @Column(name = "event_banner_url")
    private String event_banner_url;

    @NotNull
    @Column(name = "event_seatmap_url")
    private String event_seatmap_url;

    @NotNull
    @Column(name = "event_location")
    private String event_location;

    @JsonManagedReference
    @OneToMany(mappedBy = "order",
        orphanRemoval = true,
        cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

}
