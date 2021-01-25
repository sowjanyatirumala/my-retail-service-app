package com.myretail.myretailservicewebapp.domain

import spock.lang.Specification

class ProductSpec extends Specification {

    def 'validate constructor'() {
        setup:
        Long id = 123
        String name = 'test'
        Double price = 100.0
        String currencyCode = 'USD'

        when:
        final Product product = new Product(id, name, price, currencyCode)

        then:
        product
        product.id == id
        product.name == name
        product.price == price
        product.currencyCode == currencyCode
    }
}
