package org.example.virtual_wallet.controllers.Mvc;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.identity.VerificationSession;
import com.stripe.param.identity.VerificationSessionCreateParams;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
public class IdentityMvcController {
    @GetMapping("/create-verification-session")
    public String verifyUserIdentity() {
        return "verification";
    }

    @PostMapping("/create-verification-session")
    public ResponseEntity<Map<String, String>> createVerificationObject() throws StripeException {
        Stripe.apiKey = "sk_test_51OnmGhDmJUkFXVmIja9A5hf1B0czvxYcdSZeHWQF9dLKYKv3BDs4KnqHXwqkHWSz5pyUOVzYLaEU8Wz69aUP10A900QPBpu8mk";

        VerificationSessionCreateParams params = VerificationSessionCreateParams.builder()
                .setType(VerificationSessionCreateParams.Type.DOCUMENT)
                .putMetadata("user_id", "{{USER_ID}}")
                .build();

        VerificationSession verificationSession = VerificationSession.create(params);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("client_secret", verificationSession.getClientSecret());

        return ResponseEntity.ok(responseBody);
    }
}
