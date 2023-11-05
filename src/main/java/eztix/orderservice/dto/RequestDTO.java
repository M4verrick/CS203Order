package eztix.orderservice.dto;
import com.stripe.model.Product;
import lombok.Data;

@Data
public class RequestDTO {
    Product[] items;

}