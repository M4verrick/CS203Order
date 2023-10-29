package eztix.orderservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

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
    private String ticket_type;

    @NotNull
    @Column(name = "quantity")
    private Integer quantity;

    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    @JsonBackReference
    @ManyToOne
    @NotNull
    @JoinColumn(name = "order_id")
    private Orders order;

}
