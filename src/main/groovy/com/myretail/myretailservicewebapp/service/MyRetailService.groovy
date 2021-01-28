package com.myretail.myretailservicewebapp.service

import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.mappers.ProductMapper
import com.myretail.myretailservicewebapp.model.ProductRepository
import com.myretail.myretailservicewebapp.model.dto.ProductDto
import org.json.JSONException
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

    Map<String, Object> parse(JSONObject json , Map<String, Object> jsonMap) throws JSONException {
        Iterator<String> keys = json.keys()

        while (keys.hasNext()) {
            String key = keys.next()
            String val = null

            try {
                JSONObject value = json.getJSONObject(key)
                parse(value, jsonMap)
            } catch(Exception e) {
                val = json.getString(key)
            }

            if(val != null){
                jsonMap.put(key, val)
            }
        }

        return jsonMap
    }

    String getProductNameFromJson(String jsonResponse) {
        return parse(new JSONObject(jsonResponse), new HashMap<String, Object>()).get("title")
    }
}
