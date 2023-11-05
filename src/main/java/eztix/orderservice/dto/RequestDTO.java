package eztix.orderservice.dto;
import com.stripe.model.Product;


public class RequestDTO {
    Product[] items;

    public Product[] getItems() {
        return items;
    }


}