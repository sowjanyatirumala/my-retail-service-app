package com.myretail.myretailservicewebapp.domain

import io.swagger.annotations.ApiModelProperty

class Product {
    @ApiModelProperty(notes = 'Product ID', required = true)
    Long id

    @ApiModelProperty(notes = 'Name of the product')
    String name

    @ApiModelProperty(notes = 'Price of the product (with currency code)')
    CurrentPrice currentPrice

    Product() {
    }

    Product(Long id, String name, CurrentPrice currentPrice) {
        this.id = id
        this.name = name
        this.currentPrice = currentPrice
    }
}
