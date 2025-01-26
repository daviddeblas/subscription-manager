package com.subscription_manager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Subscription {
    private final String brandName;
    private final String fromAddress;

    @Setter
    private String unsubscribeLink;
    @Setter
    private String lastSubject;

    public Subscription(String brandName, String fromAddress) {
        this.brandName = brandName;
        this.fromAddress = fromAddress;
    }
}
