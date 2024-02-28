package org.example.virtual_wallet.controllers.Rest;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.identity.VerificationSession;
import com.stripe.param.identity.VerificationSessionCreateParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/identity")
public class IdentityRestController {

    @PostMapping("/create-verification-session-rest")
    public String createVerificationSession() throws StripeException {
        Stripe.apiKey = "sk_test_51OnmGhDmJUkFXVmIja9A5hf1B0czvxYcdSZeHWQF9dLKYKv3BDs4KnqHXwqkHWSz5pyUOVzYLaEU8Wz69aUP10A900QPBpu8mk";

        // In the route handler for /create-verification-session:
        // Authenticate your user

        VerificationSessionCreateParams params = VerificationSessionCreateParams.builder()
                .setType(VerificationSessionCreateParams.Type.DOCUMENT)
                .putMetadata("user_id", "{{USER_ID}}")
                .build();

        // Create the session
        VerificationSession verificationSession = VerificationSession.create(params);

        // Return only the client secret to the frontend.
        String clientSecret = verificationSession.getClientSecret();

        return clientSecret;
    }



//    @PostMapping("/handle-verificaiton")
//    public String handleVerificationSession(@RequestBody String eventId) throws StripeException {
//
//        Event event
//
//    }


}