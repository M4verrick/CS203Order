package eztix.orderservice.unit;

import eztix.orderservice.dto.OrderDTO;
import eztix.orderservice.dto.OrderItemDTO;
import eztix.orderservice.exception.RequestValidationException;
import eztix.orderservice.exception.ResourceNotFoundException;
import eztix.orderservice.model.OrderItem;
import eztix.orderservice.model.PaymentOrder;
import eztix.orderservice.repository.OrderRepository;
import eztix.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService testOrderService;

    // if payment order ID not in database throws resource not found exception
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

    // if id exists then no error thrown when retrieve payment order by id
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
    public void givenValidCustomerAndPurchaseRequestId_whenAdd_thenSuccessful() {
        // create a sample OrderDTO
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomer_id("customerId");
        orderDTO.setPurchase_request_id(1L);

        // create a sample list of OrderItemDTOs
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();

        // add OrderItemDTOs to the list
        orderDTO.setOrderItems(orderItemDTOList);

        // create a sample PaymentOrder
        PaymentOrder paymentOrder = PaymentOrder.builder()
                .customerId(orderDTO.getCustomer_id())
                .purchaseRequestId(orderDTO.getPurchase_request_id())
                .build();

        // create a list of OrderItem entities from OrderItemDTOs
        List<OrderItem> orderItems = new ArrayList<>();

        // add OrderItems to the list
        paymentOrder.setOrderItems(orderItems);

        // mock the behavior of the orderRepository
        when(orderRepository.save(any(PaymentOrder.class))).thenReturn(paymentOrder);

        // call the service method
        PaymentOrder result = testOrderService.addNewOrder(orderDTO);

        // assert the result
        assertEquals(paymentOrder, result);
    }

    // if customer id is null then throw request validation exception when adding new payment order
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

    // if purchase request id is null throw request validation exception
    @Test
    public void givenNullPurchaseRequestId_whenAdd_throwRequestValidationException(){
        // given
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomer_id("john");
        orderDTO.setPurchase_request_id(null);

        // when
        // then
        assertThatThrownBy(()-> testOrderService.addNewOrder(orderDTO))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("purchase request id cannot be null");
    }
}
