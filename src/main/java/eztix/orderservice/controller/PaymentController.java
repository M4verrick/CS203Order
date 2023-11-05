package eztix.orderservice.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import eztix.orderservice.dto.ProductDAO;
import eztix.orderservice.dto.RequestDTO;
import eztix.orderservice.pojo.CustomerUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class PaymentController {

    String STRIPE_API_KEY = System.getenv().get("STRIPE_API_KEY");


    @GetMapping("/api/v1/test")
    Object test(JwtAuthenticationToken token) {
        return token;
    }

    @PostMapping("/api/v1/checkout/hosted")
    String hostedCheckout(@RequestBody RequestDTO requestDTO, Authentication authentication, JwtAuthenticationToken token) throws StripeException {

        Stripe.apiKey = STRIPE_API_KEY;
        String clientBaseURL = System.getenv().get("CLIENT_BASE_URL");

        Customer customer = CustomerUtil.findOrCreateCustomer((String) token.getTokenAttributes().get("email"), authentication.getName());

        SessionCreateParams.Builder paramsBuilder =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setCustomer(customer.getId())
                            .setSuccessUrl(clientBaseURL + "/success?session_id={CHECKOUT_SESSION_ID}")
                            .setCancelUrl(clientBaseURL + "/failure");

            for (Product product : requestDTO.getItems()) {
                paramsBuilder.addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        PriceData.builder()
                                                .setProductData(
                                                        PriceData.ProductData.builder()
                                                                .putMetadata("app_id", product.getId())
                                                                .setName(product.getName())
                                                                .build()
                                                )
                                                .setCurrency(ProductDAO.getProduct(product.getId()).getDefaultPriceObject().getCurrency())
                                                .setUnitAmountDecimal(ProductDAO.getProduct(product.getId()).getDefaultPriceObject().getUnitAmountDecimal())
                                                .build())
                                .build());
            }

        Session session = Session.create(paramsBuilder.build());

        return session.getUrl();
    }



}
