package com.myretail.myretailservicewebapp.mappers

import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.myretailservicewebapp.domain.CurrentPrice
import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.model.dto.CurrentPriceClobDto
import com.myretail.myretailservicewebapp.model.dto.ProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ProductMapper {

    @Autowired
    ObjectMapper objectMapper

    Product mapProductFromCassRecord(ProductDto productDto) {
        if (!productDto)
            return null

        return productDto.with {
            new Product(id, null, mapCurrentPriceFromClob(currentPrice))
        }
    }

    CurrentPrice mapCurrentPriceFromClob(String currentPrice) {
        if (!currentPrice)
            return null

        CurrentPriceClobDto clobDto = objectMapper.readValue(currentPrice, CurrentPriceClobDto)
        return clobDto.with {
            return new CurrentPrice(price, currencyCode)
        }
    }

    String mapCurrentPrice(CurrentPrice currentPrice) {
        if (!currentPrice)
            return null

        return currentPrice.with {
            final CurrentPriceClobDto clobDto = new CurrentPriceClobDto(price: price, currencyCode: currencyCode)
            return objectMapper.writeValueAsString(clobDto)
        }
    }

    ProductDto mapCassRecordFromProduct(Product product) {
        if (!product)
            return null

        return product.with {
            new ProductDto(
                    id: id,
                    currentPrice: mapCurrentPrice(currentPrice)
            )
        }
    }
}
