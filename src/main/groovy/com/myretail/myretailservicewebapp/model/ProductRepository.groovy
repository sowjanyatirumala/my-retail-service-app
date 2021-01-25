package com.myretail.myretailservicewebapp.model

import com.myretail.myretailservicewebapp.model.dto.ProductDto
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository extends CassandraRepository<ProductDto, Long> {
    ProductDto findById(long id)
}