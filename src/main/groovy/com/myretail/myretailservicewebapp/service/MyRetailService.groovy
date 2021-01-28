package com.myretail.myretailservicewebapp.service

import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.mappers.ProductMapper
import com.myretail.myretailservicewebapp.model.ProductRepository
import com.myretail.myretailservicewebapp.model.dto.ProductDto
import org.json.JSONObject
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
        //TODO - fix the logic to dynamically get the product name
        JSONObject jsonObject = new JSONObject(jsonResponse)

        return jsonObject.getJSONObject("product")?.getJSONObject("item")?.getJSONObject("product_description")?.get("title")
    }
}
