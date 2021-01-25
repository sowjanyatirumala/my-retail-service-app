package com.myretail.myretailservicewebapp.mappers

import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.model.dto.ProductDto
import org.springframework.stereotype.Component

@Component
class ProductMapper {
    Product mapDatabaseRecord(ProductDto productDto) {
        if (!productDto)
            return null

        return productDto.with {
            new Product(id, null, price, currencyCode)
        }
    }

    ProductDto mapProduct(Product product) {
        if (!product)
            return null

        return product.with {
            new ProductDto(
                    id: id,
                    price: price,
                    currencyCode: currencyCode
            )
        }
    }
}
