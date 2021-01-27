package com.myretail.myretailservicewebapp.service

import com.myretail.myretailservicewebapp.domain.CurrentPrice
import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.model.ProductRepository
import com.myretail.myretailservicewebapp.model.dto.ProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class MyRetailServiceIntegrationSpec extends Specification {

    @Autowired
    MyRetailService myRetailService

    @Autowired
    ProductRepository productRepository

    def 'get product details - happy path'() {
        setup:
        Long id = 123
        String name = 'test product'
        Double price = 150.0
        String currencyCode = 'USD'
        def productDto = new ProductDto(id: id, currentPrice: """{"value":$price,"currencyCode":"$currencyCode"}""")
        if (recordExists) {
            productRepository.save(productDto)
        }

        when:
        def product = myRetailService.getProductDetails(id, name)

        then:
        if (recordExists) {
            assert product
            assert product.id == id
            assert product.name == name
            assert product.currentPrice
            assert product.currentPrice.value == price
            assert product.currentPrice.currencyCode == currencyCode
        } else {
            assert !product
        }

        cleanup:
        productRepository.delete(productDto)

        where:
        recordExists << [false, true]
    }

    def 'set product details - happy path'() {
        setup:
        def product = new Product(123, 'test product', new CurrentPrice(125.0, 'USD'))
        def productDto = new ProductDto(id: 123, currentPrice: """{"value":200.0,"currencyCode":"USD"}""")
        if (recordExists) {
            productRepository.save(productDto)
        }

        when:
        def resultDto = myRetailService.setProductData(product)

        then:
        resultDto
        resultDto.id == product.id
        resultDto.currentPrice == """{"value":125.0,"currencyCode":"USD"}"""

        cleanup:
        productRepository.delete(productDto)

        where:
        recordExists << [false, true]
    }
}
