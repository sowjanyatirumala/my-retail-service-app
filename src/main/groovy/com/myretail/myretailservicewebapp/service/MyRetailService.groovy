package com.myretail.myretailservicewebapp.service

import com.myretail.myretailservicewebapp.domain.Product
import org.springframework.stereotype.Service

@Service
class MyRetailService {
    Product getProductDetails(long productId) {
        return new Product(productId, null, null, null)
    }
}
