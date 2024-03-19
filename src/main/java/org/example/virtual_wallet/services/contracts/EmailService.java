package org.example.virtual_wallet.services.contracts;

public interface EmailService {
    void sendEmail(String to, String subject, String message);
    void sendTransactionEmail(String to, String message);
}
