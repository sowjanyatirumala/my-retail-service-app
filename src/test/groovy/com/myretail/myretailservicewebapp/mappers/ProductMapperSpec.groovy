package com.myretail.myretailservicewebapp.mappers

import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.myretailservicewebapp.domain.CurrentPrice
import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.model.dto.ProductDto
import spock.lang.Specification

class ProductMapperSpec extends Specification {
    ProductMapper productMapper = new ProductMapper()
    ObjectMapper objectMapper = new ObjectMapper()

    def setup() {
        productMapper.objectMapper = objectMapper
    }

    def 'map product domain object from cassandra record - no record'() {
        expect:
        !productMapper.mapProductFromCassRecord(null)
    }

    def "map product domain object from cassandra record - happy path"() {
        setup:
        ProductMapper mapperSpy = GroovySpy()
        mapperSpy.objectMapper = objectMapper

        def productDto = new ProductDto(id: 123, currentPrice: """{"value": 100, "currencyCode": "USD"}""")
        def expectedCurrentPrice = Mock(CurrentPrice)

        when:
        def product = mapperSpy.mapProductFromCassRecord(productDto)

        then:
        1 * mapperSpy.mapProductFromCassRecord(productDto)
        1 * mapperSpy.mapCurrentPriceFromClob(productDto.currentPrice) >> expectedCurrentPrice
        0 * _

        and:
        product
        product.id == productDto.id
        !product.name
        product.currentPrice == expectedCurrentPrice
    }

    def 'map current price domain object from string - null value'() {
        expect:
        !productMapper.mapCurrentPriceFromClob(null)
    }

    def 'map current price domain object from string - happy path'() {
        setup:
        Double price = 100
        String currencyCode = 'USD'
        String currentPriceClob = """{"value": $price, "currencyCode": "$currencyCode"}"""

        when:
        def currentPrice = productMapper.mapCurrentPriceFromClob(currentPriceClob)

        then:
        currentPrice
        currentPrice.value == price
        currentPrice.currencyCode == currencyCode
    }

    def 'map current price string from domain object - null value'() {
        expect:
        !productMapper.mapCurrentPrice(null)
    }

    def 'map current price string from domain object - happy path'() {
        setup:
        Double price = 200.0
        String currencyCode = 'USD'
        def currentPrice = new CurrentPrice(price, currencyCode)
        String expectedString = """{"value":$price,"currencyCode":"$currencyCode"}"""

        when:
        def currentPriceString = productMapper.mapCurrentPrice(currentPrice)

        then:
        currentPriceString == expectedString
    }

    def 'map product cassandra record from domain object - null value'() {
        expect:
        !productMapper.mapCassRecordFromProduct(null)
    }

    def "map product domain object to database dto - happy path"() {
        setup:
        ProductMapper mapperSpy = GroovySpy()
        mapperSpy.objectMapper = objectMapper

        Long id = 123
        CurrentPrice currentPrice = new CurrentPrice()
        def product = new Product(id, null, currentPrice)
        def expectedCurrentPrice = "current price"

        when:
        def productDto = mapperSpy.mapCassRecordFromProduct(product)

        then:
        1 * mapperSpy.mapCassRecordFromProduct(product)
        1 * mapperSpy.mapCurrentPrice(product.currentPrice) >> expectedCurrentPrice
        0 * _

        and:
        productDto
        productDto.id == product.id
        productDto.currentPrice == expectedCurrentPrice
    }
}
