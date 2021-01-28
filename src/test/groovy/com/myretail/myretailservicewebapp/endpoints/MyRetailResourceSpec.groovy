package com.myretail.myretailservicewebapp.endpoints

import com.myretail.myretailservicewebapp.domain.CurrentPrice
import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.model.dto.ProductDto
import com.myretail.myretailservicewebapp.service.MyRetailService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spock.lang.Specification
import java.lang.reflect.Method

class MyRetailResourceSpec extends Specification {
    MyRetailResource resource = new MyRetailResource()
    MyRetailService mockMyRetailService = Mock()

    def setup() {
        resource.myRetailService = mockMyRetailService
    }

    def 'test class annotations'() {
        expect:
        MyRetailResource.declaredAnnotations.find {it.annotationType() == RestController }
        MyRetailResource.declaredAnnotations.find {it.annotationType() == RequestMapping }
    }

    def 'get product details - test method annotations'() {
        setup:
        Method method = MyRetailResource.getMethod('getProductDetails', Long)

        expect:
        method?.declaredAnnotations?.find { it.annotationType() == GetMapping }
    }

    def 'get product details - throws 400 error when invalid product Id is sent in the request'() {
        setup:
        MyRetailResource resourceSpy = GroovySpy()
        Long id = 123
        String testErrorMessage = 'test error message'

        when:
        def result = resourceSpy.getProductDetails(id)

        then:
        1 * resourceSpy.getProductDetails(id)
        1 * resourceSpy.getProductNameFromService(id) >> { throw new IllegalArgumentException(testErrorMessage) }
        0 * _

        and:
        result
        result.statusCode == HttpStatus.BAD_REQUEST
        result.statusCodeValue == 400
        result.body == testErrorMessage
    }

    def 'get product details - throws 404 error when the product price information is not available in cassandra table'() {
        setup:
        MyRetailResource resourceSpy = GroovySpy()
        resourceSpy.myRetailService = mockMyRetailService
        Long id = 123
        String productName = 'test product'

        when:
        def result = resourceSpy.getProductDetails(id)

        then:
        1 * resourceSpy.getProductDetails(id)
        1 * resourceSpy.getProductNameFromService(id) >> productName
        1 * mockMyRetailService.getProductDetails(id, productName) >> null
        0 * _

        and:
        result
        result.statusCode == HttpStatus.NOT_FOUND
        result.statusCodeValue == 404
        result.body == 'Product not found in the data store'
    }

    def 'get product details - handles exceptions'() {
        setup:
        MyRetailResource resourceSpy = GroovySpy()
        resourceSpy.myRetailService = mockMyRetailService
        Long id = 123
        String productName = 'test product'
        String testErrorMessage = 'test error message'

        when:
        def result = resourceSpy.getProductDetails(id)

        then:
        1 * resourceSpy.getProductDetails(id)
        1 * resourceSpy.getProductNameFromService(id) >> productName
        1 * mockMyRetailService.getProductDetails(id, productName) >> {  throw new Exception(testErrorMessage) }
        0 * _

        and:
        result
        result.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        result.statusCodeValue == 500
        result.body == testErrorMessage
    }

    def 'get product details - happy path'() {
        setup:
        MyRetailResource resourceSpy = GroovySpy()
        resourceSpy.myRetailService = mockMyRetailService
        Long id = 123
        String name = 'test product'
        Product product = new Product()

        when:
        def result = resourceSpy.getProductDetails(id)

        then:
        1 * resourceSpy.getProductDetails(id)
        1 * resourceSpy.getProductNameFromService(id) >> name
        1 * mockMyRetailService.getProductDetails(id, name) >> product
        0 * _

        and:
        result
        result.statusCode == HttpStatus.OK
        result.statusCodeValue == 200
        result.body == product
    }

    def 'update product price - handles invalid product details'() {
        setup:
        MyRetailResource resourceSpy = GroovySpy()
        Long id = 123

        when:
        def response = resourceSpy.updateProductPrice(id, productDetails)

        then:
        1 * resourceSpy.updateProductPrice(id, productDetails)
        0 * _

        and:
        response
        response.statusCode == HttpStatus.BAD_REQUEST
        response.statusCodeValue == 400
        response.body == 'Invalid product details, the ID in the json body should match the product ID in the request.'

        where:
        productDetails << [null, new Product(1, 'test', new CurrentPrice())]
    }

    def 'update product price - handles invalid product price'() {
        setup:
        MyRetailResource resourceSpy = GroovySpy()
        Long id = 123
        Product productDetails = new Product(id, 'test', currentPrice)

        when:
        def response = resourceSpy.updateProductPrice(id, productDetails)

        then:
        1 * resourceSpy.updateProductPrice(id, productDetails)
        0 * _

        and:
        response
        response.statusCode == HttpStatus.BAD_REQUEST
        response.statusCodeValue == 400
        response.body == 'Invalid price, the product price cannot be null.'

        where:
        currentPrice << [null, new CurrentPrice(), new CurrentPrice(null, null)]
    }

    def 'update product price - handles exceptions'() {
        setup:
        MyRetailResource resourceSpy = GroovySpy()
        resourceSpy.myRetailService = mockMyRetailService
        Long id = 123
        Product productDetails = new Product(id, 'test', new CurrentPrice(10, 'USD'))
        def testErrorMessage = 'test error message'

        when:
        def response = resourceSpy.updateProductPrice(id, productDetails)

        then:
        1 * resourceSpy.updateProductPrice(id, productDetails)
        1 * mockMyRetailService.setProductPrice(productDetails) >> { throw new Exception(testErrorMessage) }
        0 * _

        and:
        response
        response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        response.statusCodeValue == 500
        response.body == testErrorMessage
    }

    def 'update product price - happy path'() {
        setup:
        MyRetailResource resourceSpy = GroovySpy()
        resourceSpy.myRetailService = mockMyRetailService
        Long id = 123
        Double price = 100.0
        String currencyCode = 'USD'
        Product productDetails = new Product(id, 'test', new CurrentPrice(price, currencyCode))
        ProductDto productDto = new ProductDto(id: id, currentPrice: """{"value": $price, "currencyCode": "$currencyCode"}""")

        when:
        def response = resourceSpy.updateProductPrice(id, productDetails)

        then:
        1 * resourceSpy.updateProductPrice(id, productDetails)
        1 * mockMyRetailService.setProductPrice(productDetails) >> productDto
        0 * _

        and:
        response
        response.statusCode == HttpStatus.OK
        response.statusCodeValue == 200
        response.body == productDto
    }

    def 'get product name from service - invalid product id'() {
        setup:
        Long id = 123

        when:
        resource.getProductNameFromService(id)

        then:
        IllegalArgumentException iae = thrown()
        iae.message == 'Product not found in redsky target service, please enter a valid product id.'
    }
}
