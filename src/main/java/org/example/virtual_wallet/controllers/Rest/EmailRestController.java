package org.example.virtual_wallet.controllers.Rest;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/email")
public class EmailRestController {

    @PostMapping("/send")
    public void sendMail() throws IOException {
        //todo read for env variable!
        String key ="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";
        Email from = new Email("flexpayggk@gmail.com");
        String subject = "Flex Pay confirmation code";
        //imeila predopolagam shte se vzeme ot sesiqta
        Email to = new Email("nikol.moldovanova02@gmail.com");
        Content content = new Content("text/plain", "Поздравления печелиш Iphone3012 ultra mega giga max pro galaxy");
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(key);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}
