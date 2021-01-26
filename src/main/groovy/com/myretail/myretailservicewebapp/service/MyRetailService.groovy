package com.myretail.myretailservicewebapp.service

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

    Product getProductDetails(long productId) {
        def product = productMapper.mapDatabaseRecord(productRepository.findById(productId))
        //TODO - get product name from service and set to product domain object

        return product
    }

    ProductDto setProductData(Product product) {
        def productDto = productMapper.mapProduct(product)
        return productRepository.save(productDto)
    }
}
