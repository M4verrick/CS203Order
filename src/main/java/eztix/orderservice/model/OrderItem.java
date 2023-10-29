package eztix.orderservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "order_item")
@Table(name = "ORDER_ITEM")
public class OrderItem {

    @Id
    @SequenceGenerator(name = "order_item_sequence", sequenceName = "order_item_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_sequence")
    @Schema(hidden = true)
    private Long id;

    @NotNull
    @Column(name = "ticket_type")
    private String ticketType;

    @NotNull
    @Column(name = "quantity")
    private Integer quantity;

    @NotNull
    @Column(name = "price")
    private Double price;

    @NotNull
    @Column(name = "start_time")
    private OffsetDateTime startTime;

    @NotNull
    @Column(name = "end_time")
    private OffsetDateTime endTime;

    @JsonBackReference
    @ManyToOne
    @NotNull
    @JoinColumn(name = "payment_order_id")
    private PaymentOrder paymentOrder;

}
