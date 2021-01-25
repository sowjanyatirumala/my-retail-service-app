package com.myretail.myretailservicewebapp.domain

class Product {
    Long id
    String name
    Double price
    String currencyCode

    Product(Long id, String name, Double price, String currencyCode) {
        this.id = id
        this.name = name
        this.price = price
        this.currencyCode = currencyCode
    }
}
