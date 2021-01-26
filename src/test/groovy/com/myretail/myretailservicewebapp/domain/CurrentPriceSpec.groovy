package com.myretail.myretailservicewebapp.domain

import spock.lang.Specification

class CurrentPriceSpec extends Specification {

    def 'validate constructor'() {
        setup:
        Double price = 100.0
        String currencyCode = 'USD'

        when:
        CurrentPrice currentPrice = new CurrentPrice(price, currencyCode)

        then:
        currentPrice
        currentPrice.price == price
        currentPrice.currencyCode == currencyCode
    }
}
