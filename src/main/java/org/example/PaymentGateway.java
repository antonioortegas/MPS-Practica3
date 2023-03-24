package org.example;

public interface PaymentGateway {
    boolean advertiserHasFunds(String advertiserName);

    void chargeAdvertiser(String advertiserName);
}
