package eztix.orderservice.repository;

import eztix.orderservice.model.Orders;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Orders,Long>{
}
