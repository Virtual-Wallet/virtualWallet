package org.example.virtual_wallet.services;

import org.example.virtual_wallet.services.contracts.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final static String TRANSACTION_VERIFICATION_URL = "http://localhost:8080/cards/verify";
    private final static String EMAIL_VERIFICATION_URL = "http://localhost:8080/authentication/verify";
    private static final String EMAIL_SUBJECT = "Verification code from Flex Pay";


    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        String content = "<a href='" + EMAIL_VERIFICATION_URL + "'>Verify</a>";

        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText("Please confirm your email using the token: " + message);
        simpleMailMessage.setText("The token for your email confirmation is: " + message
                + "\n Please click on the following link to complete your registration: \n" + content
                + "\n Please note the token will expire in 5 min!");

       this.mailSender.send(simpleMailMessage);
    }

    @Override
    public void sendTransactionEmail(String to, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        String content = "<a href='" + TRANSACTION_VERIFICATION_URL + "'>Verify</a>";

        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(EMAIL_SUBJECT);
        simpleMailMessage.setText("The token for your large transaction is: " + message
        + "\n Please click on the following link to complete your transaction: \n" + content
                        + "\n Please note the token will expire in 5 min!");

        this.mailSender.send(simpleMailMessage);
    }

}
