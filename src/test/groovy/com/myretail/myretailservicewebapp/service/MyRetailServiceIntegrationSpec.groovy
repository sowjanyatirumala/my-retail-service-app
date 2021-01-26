package com.myretail.myretailservicewebapp.service

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
        def productDto = new ProductDto(id: id, price: 150.0, currencyCode: 'USD')
        if (recordExists) {
            productRepository.save(productDto)
        }

        when:
        def product = myRetailService.getProductDetails(id)

        then:
        if (recordExists) {
            assert product
            assert product.id == id
            assert !product.name
            assert product.price == productDto.price
            assert product.currencyCode == productDto.currencyCode
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
        def product = new Product(123, 'test product', 125.0, 'USD')
        def productDto = new ProductDto(id: 123, price: 150.0, currencyCode: 'USD')
        if (recordExists) {
            productRepository.save(productDto)
        }

        when:
        def resultDto = myRetailService.setProductData(product)

        then:
        resultDto
        resultDto.id == product.id
        resultDto.price == product.price
        resultDto.currencyCode == product.currencyCode

        cleanup:
        productRepository.delete(productDto)

        where:
        recordExists << [false, true]
    }
}
