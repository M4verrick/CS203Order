package eztix.orderservice.unit;

import eztix.orderservice.dto.OrderDTO;
import eztix.orderservice.exception.RequestValidationException;
import eztix.orderservice.exception.ResourceNotFoundException;
import eztix.orderservice.model.Orders;
import eztix.orderservice.model.PaymentOrder;
import eztix.orderservice.repository.OrderRepository;
import eztix.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService testOrderService;

    @Test
    public void givenIdNotInDB_whenRetrieveByOrderId_throwResourceNotFoundException(){

        // given
        given(orderRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testOrderService.getOrderById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("order with id %d does not exist", 1L);
    }

    @Test
    public void givenOrderExist_whenRetrieve_thenSuccessful(){
        // given
        PaymentOrder order = new PaymentOrder();
        given(orderRepository.findById(order.getId()))
                .willReturn(Optional.of(order));

        // when
        PaymentOrder retrievedOrder = testOrderService
                .getOrderById(order.getId());
        // then
        assertThat(retrievedOrder).isEqualTo(order);
    }
    @Test
    public void givenNullCustomerId_whenAdd_throwRequestValidationException(){
        // given
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomer_id(null);

        // when
        // then
        assertThatThrownBy(()->testOrderService.addNewOrder(orderDTO))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("customer id cannot be null");
    }

    @Test
    public void givenNullPurchaseRequestId_whenAdd_throwRequestValidationException(){
        // given
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomer_id(1L);
        orderDTO.setPurchase_request_id(null);

        // when
        // then
        assertThatThrownBy(()-> testOrderService.addNewOrder(orderDTO))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("purchase request id cannot be null");
    }
}
