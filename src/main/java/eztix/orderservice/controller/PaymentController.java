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

    // test endpoint for JWT authentication
    @GetMapping("/api/v1/test")
    Object test(JwtAuthenticationToken token) {
        return token;
    }

    // endpoint for hosted checkout using Stripe
    @PostMapping("/api/v1/checkout/hosted")
    String hostedCheckout(@RequestBody RequestDTO requestDTO, Authentication authentication, JwtAuthenticationToken token) throws StripeException {
        // set stripe API key
        Stripe.apiKey = STRIPE_API_KEY;

        // get the client's base URL from environment variables
        String clientBaseURL = System.getenv().get("CLIENT_BASE_URL");

        // find or create a customer using email and name
        Customer customer = CustomerUtil.findOrCreateCustomer((String) token.getTokenAttributes().get("email"), authentication.getName());

        // build the parameters for the hosted checkout session
        SessionCreateParams.Builder paramsBuilder =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setCustomer(customer.getId())
                            .setSuccessUrl(clientBaseURL + "/success?session_id={CHECKOUT_SESSION_ID}")
                            .setCancelUrl(clientBaseURL + "/failure");

        // add line items for each product in the request
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
        // create the stripe session
        Session session = Session.create(paramsBuilder.build());

        return session.getUrl();
    }



}
