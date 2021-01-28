package com.myretail.myretailservicewebapp.endpoints

import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.service.MyRetailService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.ws.rs.NotFoundException

@RestController
@RequestMapping(path = '/products')
@Api(value = 'myretailservice', tags = ['Operations pertaining to my retail product store'])
class MyRetailResource extends ResponseEntityExceptionHandler {

    @Autowired
    MyRetailService myRetailService

    @GetMapping(path = '/{id}', produces = 'application/json')
    @ApiOperation(value = 'Get the product details')
    @ApiResponses(value = [
            @ApiResponse(code = 200, message = 'Product found on the store', response = Product ),
            @ApiResponse(code = 400, message = 'Bad request'),
            @ApiResponse(code = 404, message = 'Product not found')
    ])
    ResponseEntity getProductDetails(
            @PathVariable('id') Long id
    ) {
        try {
            def productName = getProductNameFromService(id)

            def product = myRetailService.getProductDetails(id, productName)
            if (!product)
                throw new NotFoundException('Product not found in the data store')

            return ResponseEntity.ok(product)
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.message)
        } catch (NotFoundException nfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nfe.message)
        } catch (Throwable e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = 'Update the product price in data store')
    @ApiResponses(value = [
            @ApiResponse(code = 204, message = 'Product price saved'),
            @ApiResponse(code = 400, message = 'Bad request')
    ])
    ResponseEntity updateProductPrice(@PathVariable("id") Long productId,
                                    @RequestBody Product productDetails) {
        try {
            if (!productDetails || (productDetails.id != productId))
                throw new IllegalArgumentException('Invalid product details, the ID in the json body should match the product ID in the request.')

            if (!productDetails.currentPrice || !productDetails.currentPrice.value)
                throw new IllegalArgumentException('Invalid price, the product price cannot be null.')

            return ResponseEntity.ok(myRetailService.setProductPrice(productDetails))
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.message)
        } catch (Throwable e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }

    String getProductNameFromService(Long id) {
        String uri = "https://redsky.target.com/v3/pdp/tcin/{id}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics&key=candidate"
        RestTemplate restTemplate = new RestTemplate()

        Map<String, Long> params = new HashMap<String, Long>()
        params.put("id", id)

        try {
            def jsonResponse = restTemplate.getForObject(uri, String, params)
            return myRetailService.getProductNameFromJson(jsonResponse)
        } catch (Exception e) {
            throw new IllegalArgumentException('Product not found in redsky target service, please enter a valid product id.', e)
        }
    }
}