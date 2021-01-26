package com.myretail.myretailservicewebapp.model

import com.myretail.myretailservicewebapp.model.dto.ProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ProductRepositoryIntegrationSpec extends Specification {

    @Autowired
    ProductRepository productRepository

    def 'save product record'() {
        setup:
        Long id = 123
        def productDto = new ProductDto(id: 123, currentPrice: """{"price": 100, "currencyCode": "USD"}""")

        when:
        def cassandraEntity = productRepository.save(productDto)

        then:
        cassandraEntity
        cassandraEntity.id == id
        cassandraEntity.currentPrice == productDto.currentPrice

        cleanup:
        productRepository.delete(productDto)
    }

    def 'get product record by id'() {
        setup:
        Long id = 123
        def productDto = productRepository.save(new ProductDto(id: 123, currentPrice: """{"price": 100, "currencyCode": "USD"}"""))

        when:
        def cassandraEntity = productRepository.findById(id) as ProductDto

        then:
        cassandraEntity
        cassandraEntity.id == id
        cassandraEntity.currentPrice == productDto.currentPrice

        cleanup:
        productRepository.delete(productDto)
    }
}
