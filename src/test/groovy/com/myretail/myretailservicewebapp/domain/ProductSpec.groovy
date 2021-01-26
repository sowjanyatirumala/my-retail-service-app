package com.myretail.myretailservicewebapp.domain

import spock.lang.Specification

class ProductSpec extends Specification {

    def 'validate constructor'() {
        setup:
        Long id = 123
        String name = 'test'
        CurrentPrice currentPrice = new CurrentPrice()

        when:
        final Product product = new Product(id, name, currentPrice)

        then:
        product
        product.id == id
        product.name == name
        product.currentPrice == currentPrice
    }
}
