package com.myretail.myretailservicewebapp.model.dto

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table(value = 'Product')
class ProductDto {
    @PrimaryKey
    Long id

    String currentPrice
}
