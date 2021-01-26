package com.myretail.myretailservicewebapp.domain

class Product {
    Long id
    String name
    CurrentPrice currentPrice

    Product() {
    }

    Product(Long id, String name, CurrentPrice currentPrice) {
        this.id = id
        this.name = name
        this.currentPrice = currentPrice
    }
}
