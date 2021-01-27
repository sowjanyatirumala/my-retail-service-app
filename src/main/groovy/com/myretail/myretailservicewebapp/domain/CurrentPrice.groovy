package com.myretail.myretailservicewebapp.domain

class CurrentPrice {
    Double value
    String currencyCode

    CurrentPrice() {
    }

    CurrentPrice(Double value, String currencyCode) {
        this.value = value
        this.currencyCode = currencyCode
    }
}
