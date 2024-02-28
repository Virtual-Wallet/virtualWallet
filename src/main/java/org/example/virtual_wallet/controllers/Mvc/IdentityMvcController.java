package org.example.virtual_wallet.controllers.Mvc;

import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.identity.VerificationSession;
import com.stripe.net.Webhook;
import com.stripe.param.identity.VerificationSessionCreateParams;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
public class IdentityMvcController {
    @GetMapping("/create-verification-session")
    public String verifyUserIdentity() {
        return "verification";
    }


    @GetMapping("/submit-verification-session")
    public String getConfirmation() {
        return "submitted";
    }

    @PostMapping("/create-verification-session")
    public ResponseEntity<Map<String, String>>
    createVerificationObject() throws StripeException {
        Stripe.apiKey = "sk_test_51OnmGhDmJUkFXVmIja9A5hf1B0czvxYcdSZeHWQF9dLKYKv3BDs4KnqHXwqkHWSz5pyUOVzYLaEU8Wz69aUP10A900QPBpu8mk";

        VerificationSessionCreateParams params =
                VerificationSessionCreateParams.builder()
                        .setType(VerificationSessionCreateParams.Type.DOCUMENT)
                        .setOptions(
                                VerificationSessionCreateParams.Options.builder()
                                        .setDocument(
                                                VerificationSessionCreateParams.Options.Document.builder()
                                                        .setRequireMatchingSelfie(true)
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();

        VerificationSession verificationSession = VerificationSession.create(params);

        Map<String, String> responseBody = new HashMap<>();

        responseBody.put("client_secret", verificationSession.getClientSecret());

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/webhook")
    public String handleWebhook(@RequestBody String payload,
                                @RequestHeader("Stripe-Signature") String sigHeader,
                                Model model) throws StripeException {
        Stripe.apiKey = "sk_test_51OnmGhDmJUkFXVmIja9A5hf1B0czvxYcdSZeHWQF9dLKYKv3BDs4KnqHXwqkHWSz5pyUOVzYLaEU8Wz69aUP10A900QPBpu8mk";

        String endpointSecret = "whsec_321b6046a4d78a5327e1c9a97c113cdd4833a1871aa4ed09c0b61694c82f2f5d";

        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (JsonSyntaxException | SignatureVerificationException e) {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            System.out.println("Webhook error while parsing basic request.");
            return "";
        }

        VerificationSession verificationSession = null;
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        switch (event.getType()) {
            case "identity.verification_session.verified":
                // All the verification checks passed
                if (dataObjectDeserializer.getObject().isPresent()) {
                    verificationSession = (VerificationSession) dataObjectDeserializer.getObject().get();
                } else {
                    // Deserialization failed, probably due to an API version mismatch.
                    // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
                    // instructions on how to handle this case, or return an error here.
                }
                break;
            case "identity.verification_session.requires_input":
                // At least one of the verification checks failed
                if (dataObjectDeserializer.getObject().isPresent()) {
                    verificationSession = (VerificationSession) dataObjectDeserializer.getObject().get();

                    switch (verificationSession.getLastError().getCode()) {
                        case "document_unverified_other":
                            // the document was invalid
                            break;
                        case "document_expired":
                            // the document was expired
                            break;
                        case "document_type_not_supported":
                            // document type not supported
                            break;
                        default:
                            // ...
                            break;
                    }
                } else {
                    // Deserialization failed, probably due to an API version mismatch.
                    // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
                    // instructions on how to handle this case, or return an error here.
                }
                break;
            default:
                // other event type
                break;
        }
        // Response status 200:
        return "";
    }
}


//    @GetMapping("/processing-session")
//    public String processingSession() {
//        return "handleProcessingEvent";
//    }

