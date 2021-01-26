package com.myretail.myretailservicewebapp.domain

class CurrentPrice {
    Double price
    String currencyCode

    CurrentPrice() {
    }

    CurrentPrice(Double price, String currencyCode) {
        this.price = price
        this.currencyCode = currencyCode
    }
}
