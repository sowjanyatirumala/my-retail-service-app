package com.myretail.myretailservicewebapp.model

import com.myretail.myretailservicewebapp.model.dto.ProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest
import spock.lang.Specification

@DataCassandraTest
class ProductRepositoryIntegrationSpec extends Specification {

    @Autowired
    ProductRepository productRepository

    def 'save product record'() {
        setup:
        Long id = 123
        Double price = 100.0
        String currencyCode = 'USD'
        def product = new ProductDto(id: 123, price: price, currencyCode: currencyCode)

        when:
        def cassandraEntity = productRepository.save(product)

        then:
        cassandraEntity
        cassandraEntity.id == id
        cassandraEntity.price == price
        cassandraEntity.currencyCode == currencyCode

        cleanup:
        productRepository.delete(product)
    }

    def 'get product record by id'() {
        setup:
        Long id = 123
        Double price = 100.0
        String currencyCode = 'USD'
        def product = productRepository.save(new ProductDto(id: 123, price: price, currencyCode: currencyCode))

        when:
        def cassandraEntity = productRepository.findById(id) as ProductDto

        then:
        cassandraEntity
        cassandraEntity.id == id
        cassandraEntity.price == price
        cassandraEntity.currencyCode == currencyCode

        cleanup:
        productRepository.delete(product)
    }
}
