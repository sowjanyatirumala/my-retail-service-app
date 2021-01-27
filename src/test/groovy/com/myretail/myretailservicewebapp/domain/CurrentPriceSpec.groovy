package com.myretail.myretailservicewebapp.domain

import spock.lang.Specification

class CurrentPriceSpec extends Specification {

    def 'validate constructor'() {
        setup:
        Double value = 100.0
        String currencyCode = 'USD'

        when:
        CurrentPrice currentPrice = new CurrentPrice(value, currencyCode)

        then:
        currentPrice
        currentPrice.value == value
        currentPrice.currencyCode == currencyCode
    }
}
