package com.myretail.myretailservicewebapp.endpoints

import com.myretail.myretailservicewebapp.domain.CurrentPrice
import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.model.ProductRepository
import com.myretail.myretailservicewebapp.model.dto.ProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import spock.lang.Specification

@SpringBootTest
class MyRetailResourceIntegrationSpec extends Specification {

    @Autowired
    MyRetailResource resource

    @Autowired
    ProductRepository productRepository

    def 'get product details - bad request'() {
        setup:
        Long id = 123

        when:
        def result = resource.getProductDetails(id)

        then:
        result
        result.statusCode == HttpStatus.BAD_REQUEST
        result.statusCodeValue == 400
        result.body == 'Product not found in redsky target service, please enter a valid product id.'
    }

    def 'get product details - not found response'() {
        setup:
        Long id = 54456119

        when:
        def result = resource.getProductDetails(id)

        then:
        result
        result.statusCode == HttpStatus.NOT_FOUND
        result.statusCodeValue == 404
        result.body == 'Product not found in the data store'
    }

    def 'get product details - happy path'() {
        setup:
        Long id = 13264003
        Double price = 200.0
        String currencyCode = 'USD'
        def productDto = productRepository.save(new ProductDto(id: id, currentPrice: """{"value":$price,"currencyCode":"$currencyCode"}"""))

        when:
        def result = resource.getProductDetails(id)

        then:
        result
        result.statusCode == HttpStatus.OK
        result.statusCodeValue == 200
        result.body instanceof Product
        result.body.id == id
        result.body.name == 'Jif Natural Creamy Peanut Butter - 40oz'
        result.body.currentPrice
        result.body.currentPrice.value == price
        result.body.currentPrice.currencyCode == currencyCode

        cleanup:
        productRepository.delete(productDto)
    }

    def 'set product price - bad request'() {
        setup:
        Long id = 123
        Product productDetails = new Product(productId, 'test', new CurrentPrice(price, 'USD'))

        when:
        def result = resource.updateProductPrice(id, productDetails)

        then:
        result
        result.statusCode == HttpStatus.BAD_REQUEST
        result.statusCodeValue == 400

        where:
        productId | price
        456       | 100.0
        123       | null
    }

    def 'set product price - insert - happy path'() {
        setup:
        Long id = 123
        Product productDetails = new Product(id, 'test', new CurrentPrice(100.0, 'USD'))

        when:
        def result = resource.updateProductPrice(id, productDetails)

        then:
        result
        result.statusCode == HttpStatus.OK
        result.statusCodeValue == 200
    }

    def 'set product price - update - happy path'() {
        setup:
        Long id = 123
        def productDto = productRepository.save(new ProductDto(id: id, currentPrice: """{"value":200.0,"currencyCode":"USD"}"""))
        Product productDetails = new Product(id, 'test', new CurrentPrice(100.0, 'USD'))

        when:
        def result = resource.updateProductPrice(id, productDetails)

        then:
        result
        result.statusCode == HttpStatus.OK
        result.statusCodeValue == 200

        and:
        ProductDto updatedDto = productRepository.findById(productDto.id)
        updatedDto.currentPrice == """{"value":100.0,"currencyCode":"USD"}"""
    }
}
