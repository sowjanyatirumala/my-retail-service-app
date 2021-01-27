package com.myretail.myretailservicewebapp.endpoints

import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.model.dto.ProductDto
import com.myretail.myretailservicewebapp.service.MyRetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping(path = 'products')
class MyRetailResource {

    @Autowired
    MyRetailService myRetailService

    @GetMapping(path = '/{id}', produces = 'application/json')
    Product getProductDetails(
            @PathVariable('id') Long id
    ) {
        return myRetailService.getProductDetails(id, getProductNameFromService(id))
    }

    @PutMapping("/{id}")
    ProductDto updateProductDetails(@PathVariable("id") Long productId,
                                    @RequestBody Product productDetails) {
        //TODO - Add validations

        return myRetailService.setProductData(productDetails)
    }

    String getProductNameFromService(Long id) {
        String uri = "https://redsky.target.com/v3/pdp/tcin/{id}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics&key=candidate"
        RestTemplate restTemplate = new RestTemplate()

        Map<String, Long> params = new HashMap<String, Long>()
        params.put("id", id)

        return myRetailService.getProductNameFromJson(restTemplate.getForObject(uri, String, params))
    }
}