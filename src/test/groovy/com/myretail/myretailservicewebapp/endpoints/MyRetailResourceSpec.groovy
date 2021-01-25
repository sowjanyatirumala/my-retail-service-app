package com.myretail.myretailservicewebapp.endpoints

import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.service.MyRetailService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
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
        method?.declaredAnnotations?.find { it.annotationType() == ResponseBody }
    }

    def 'get product details - happy path'() {
        setup:
        Long id = 123
        Product product = new Product()

        when:
        Product result = resource.getProductDetails(id)

        then:
        1 * mockMyRetailService.getProductDetails(id) >> product
        0 * _

        and:
        result == product
    }
}
