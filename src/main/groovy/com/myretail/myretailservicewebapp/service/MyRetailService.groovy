package com.myretail.myretailservicewebapp.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.mappers.ProductMapper
import com.myretail.myretailservicewebapp.model.ProductRepository
import com.myretail.myretailservicewebapp.model.dto.ProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MyRetailService {

    @Autowired
    ProductRepository productRepository

    @Autowired
    ProductMapper productMapper

    Product getProductDetails(long productId, String productName) {
        def productDto = productRepository.findById(productId)
        if (!productDto)
            return null

        def product = productMapper.mapProductFromCassRecord(productDto)
        product.name = productName

        return product
    }

    ProductDto setProductPrice(Product product) {
        def productDto = productMapper.mapCassRecordFromProduct(product)
        return productRepository.save(productDto)
    }

    String getProductNameFromJson(String jsonResponse) {
        if (!jsonResponse)
            return null

        ObjectMapper objectMapper = new ObjectMapper()
        Map<String, Object> jsonMap = objectMapper.readValue(jsonResponse, HashMap)

        for (entry in jsonMap) {
            String strValue = objectMapper.writeValueAsString(entry.value)

            if (entry.value instanceof String && entry.key == 'title') {
                return entry.value
            } else if (entry.value instanceof Map) {
                return getProductNameFromJson(strValue)
            }
        }
    }
}
