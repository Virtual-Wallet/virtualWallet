package org.example.virtual_wallet.models.dtos;

public class WebhookDto {
    private String payload;
    private String sigHeader;

    public WebhookDto() {
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getSigHeader() {
        return sigHeader;
    }

    public void setSigHeader(String sigHeader) {
        this.sigHeader = sigHeader;
    }
}
