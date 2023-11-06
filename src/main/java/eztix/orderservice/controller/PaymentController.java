package eztix.orderservice.controller;

import com.nimbusds.jose.shaded.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.LineItemCollection;
import com.stripe.model.checkout.Session;
import com.stripe.net.HttpHeaders;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionListLineItemsParams;
import com.stripe.param.checkout.SessionRetrieveParams;
import eztix.orderservice.dto.CheckoutDTO;
import eztix.orderservice.dto.ProductDTO;
import eztix.orderservice.pojo.CustomerUtil;
import eztix.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.stripe.net.ApiResource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class PaymentController {

    private final OrderService orderService;
    private final String STRIPE_API_KEY = System.getenv().get("STRIPE_API_KEY");

    @Autowired
    public PaymentController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/api/v1/test")
    Object test(JwtAuthenticationToken token) {
        return token;
    }

    @PostMapping("/api/v1/checkout/hosted")
    public String hostedCheckout(@RequestBody CheckoutDTO checkoutDTO, Authentication authentication, JwtAuthenticationToken token) throws StripeException {

        Stripe.apiKey = STRIPE_API_KEY;
        String clientBaseURL = System.getenv().get("CLIENT_BASE_URL");

        Customer customer = CustomerUtil.findOrCreateCustomer((String) token.getTokenAttributes().get("email"), authentication.getName());

        SessionCreateParams.Builder paramsBuilder =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setCustomer(customer.getId())
                        .putMetadata("eventId", String.valueOf(checkoutDTO.getEventId()))
                        .setSuccessUrl(checkoutDTO.getSuccessUrl())
                        .setCancelUrl(checkoutDTO.getFailureUrl());

        double total = 0;

        for (ProductDTO product : checkoutDTO.getProducts()) {

            total += product.getUnitPrice() * product.getQuantity();
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(product.getQuantity())
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setUnitAmount(product.getUnitPrice()*100)
                                    .setCurrency("SGD")
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(checkoutDTO.getEventTitle() + " " + product.getTicketType() + " " + product.getDateTime())
                                            .addImage(checkoutDTO.getBannerURL())
                                            .putMetadata("ticketId", String.valueOf(product.getTicketId()))
                                            .build())
                                    .build())
                            .build());
        }

        paramsBuilder.addLineItem(
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setUnitAmount((long) (total * 10))
                                .setCurrency("SGD")
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Service Fee")
                                        .putMetadata("ticketId", "-1")
                                        .build())
                                .build())
                        .build());

        paramsBuilder.addLineItem(
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setUnitAmount(1000L)
                                .setCurrency("SGD")
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Facility Fee")
                                        .putMetadata("ticketId", "-1")
                                        .build())
                                .build())
                        .build());


        Session session = Session.create(paramsBuilder.build());

        return session.getUrl();
    }


    @PostMapping("/api/v1/stripe/webhook")
    public Object stripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) throws StripeException {

        Stripe.apiKey = STRIPE_API_KEY;

        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, "whsec_ba709742ae840a3fb41f06a6d4cf5fd56be9e186c2d625e590000adce5c8e97d");
        } catch (JsonSyntaxException e) {
            // Invalid payload
            e.printStackTrace();
            return "";
        } catch (SignatureVerificationException e) {
            // Invalid signature
            e.printStackTrace();
            return "";
        }

        System.out.println(event.getType());
        if ("checkout.session.completed".equals(event.getType())) {
            Session sessionEvent = (Session) event.getDataObjectDeserializer().getObject().get();


            SessionRetrieveParams params =
                    SessionRetrieveParams.builder()
                            .addExpand("line_items")
                            .build();

            Session session = Session.retrieve(sessionEvent.getId(), params, null);

            SessionListLineItemsParams listLineItemsParams =
                    SessionListLineItemsParams.builder()
                            .build();

            // Retrieve the session. If you require line items in the response, you may include them by expanding line_items.
            LineItemCollection lineItems = session.listLineItems(listLineItemsParams);
            orderService.addNewOrder(lineItems);
        }
        return null;
    }
}
