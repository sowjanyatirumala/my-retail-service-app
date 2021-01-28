package com.myretail.myretailservicewebapp.service

import com.myretail.myretailservicewebapp.domain.CurrentPrice
import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.mappers.ProductMapper
import com.myretail.myretailservicewebapp.model.ProductRepository
import com.myretail.myretailservicewebapp.model.dto.ProductDto
import org.springframework.stereotype.Service
import spock.lang.Specification

class MyRetailServiceSpec extends Specification {
    MyRetailService myRetailService = new MyRetailService()
    ProductRepository mockProductRepository = Mock()
    ProductMapper mockProductMapper = Mock()

    def setup() {
        myRetailService.productRepository = mockProductRepository
        myRetailService.productMapper = mockProductMapper
    }

    def 'test class has @Service annotation'() {
        expect:
        MyRetailService.declaredAnnotations.find {it.annotationType() == Service }
    }

    def "test get product details - no row in cassandra table for the id"() {
        setup:
        Long id = 123
        String productName = 'test product'

        when:
        Product result = myRetailService.getProductDetails(id, productName)

        then:
        1 * mockProductRepository.findById(id) >> null
        0 * _

        and:
        !result
    }

    def "test get product details - happy path"() {
        setup:
        Long id = 123
        String productName = 'test product'
        ProductDto mockDto = Mock()
        Product product = new Product(id, productName, new CurrentPrice())

        when:
        Product result = myRetailService.getProductDetails(id, productName)

        then:
        1 * mockProductRepository.findById(id) >> mockDto
        1 * mockProductMapper.mapProductFromCassRecord(mockDto) >> product
        0 * _

        and:
        result == product
    }

    def 'test set product price'() {
        setup:
        Long id = 123
        ProductDto mockDto = Mock()
        Product product = new Product(id, 'test product', new CurrentPrice(100.0, 'USD'))
        ProductDto updatedDto = new ProductDto(id: id, currentPrice: """{"value":200.0,"currencyCode":"USD"}""")

        when:
        ProductDto result = myRetailService.setProductPrice(product)

        then:
        1 * mockProductMapper.mapCassRecordFromProduct(product) >> mockDto
        1 * mockProductRepository.save(mockDto) >> updatedDto
        0 * _

        and:
        result == updatedDto
    }

    //TODO - add invalid json test scenarios for getProductNameFromJson method

    def 'get product name from json - happy path'() {
        setup:
        String productName = 'test product name'
        String jsonBody = """
        {
            "product": {
                "item" : {
                    "tcin": 1234,
                    "product_description": {
                        "title": "$productName",
                        "description": "test"
                    }
                },
                "price" : 100.0
            }
        }
        """

        expect:
        myRetailService.getProductNameFromJson(jsonBody) == productName
    }
}