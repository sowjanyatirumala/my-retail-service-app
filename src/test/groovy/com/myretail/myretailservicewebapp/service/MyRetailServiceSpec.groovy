package com.myretail.myretailservicewebapp.service

import com.myretail.myretailservicewebapp.domain.Product
import org.springframework.stereotype.Service
import spock.lang.Specification

class MyRetailServiceSpec extends Specification {
    MyRetailService myRetailService = new MyRetailService()

    def 'test class has @Service annotation'() {
        expect:
        MyRetailService.declaredAnnotations.find {it.annotationType() == Service}
    }

    def "test get Product Details"() {
        setup:
        Long id = 123

        when:
        Product product = myRetailService.getProductDetails(id)

        then:
        product
        product.id == id
        !product.name
        !product.price
        !product.currencyCode
    }
}