package com.myretail.myretailservicewebapp.mappers

import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.model.dto.ProductDto
import spock.lang.Specification

class ProductMapperSpec extends Specification {
    ProductMapper productMapper = new ProductMapper()

    def "map product dto to domain object - happy path"() {
        when:
        def product = productMapper.mapDatabaseRecord(dto)

        then:
        if (resultExpected) {
            assert product
            assert product.id == dto.id
            assert !product.name
            assert product.price == dto.price
            assert product.currencyCode == dto.currencyCode
        } else {
            assert !product
        }

        where:
        dto << [null, new ProductDto(id: 123, price: 10.0, currencyCode: 'USD')]
        resultExpected = dto ? true : false
    }

    def "map product domain object to database dto - happy path"() {
        when:
        def productDto = productMapper.mapProduct(product)

        then:
        if (resultExpected) {
            assert productDto
            assert productDto.id == product.id
            assert productDto.price == product.price
            assert productDto.currencyCode == product.currencyCode
        } else {
            assert !productDto
        }

        where:
        product << [null, new Product(123, 'test product', 20.0, 'USD')]
        resultExpected = product ? true : false
    }
}
