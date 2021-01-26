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

    def "test get product details"() {
        setup:
        Long id = 123
        ProductDto mockDto = Mock()
        Product product = new Product(id, 'test product', new CurrentPrice())

        when:
        Product result = myRetailService.getProductDetails(id)

        then:
        1 * mockProductRepository.findById(id) >> mockDto
        1 * mockProductMapper.mapProductFromCassRecord(mockDto) >> product
        0 * _

        and:
        result == product
    }

    def 'test set product data'() {
        setup:
        Long id = 123
        ProductDto mockDto = Mock()
        Product product = new Product(id, 'test product', new CurrentPrice(100.0, 'USD'))
        ProductDto updatedDto = new ProductDto(id: id, currentPrice: """{"price":200.0,"currencyCode":"USD"}""")

        when:
        ProductDto result = myRetailService.setProductData(product)

        then:
        1 * mockProductMapper.mapCassRecordFromProduct(product) >> mockDto
        1 * mockProductRepository.save(mockDto) >> updatedDto
        0 * _

        and:
        result == updatedDto
    }
}