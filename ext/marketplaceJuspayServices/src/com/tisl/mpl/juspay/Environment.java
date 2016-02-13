package com.tisl.mpl.juspay;

public enum Environment {

    DEVELOPMENT("http://local.api.juspay.in"),
    PRODUCTION("https://api.juspay.in"),
    SANDBOX("https://sandbox.juspay.in");

    private final String baseUrl;

    private Environment(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

}

