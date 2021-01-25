package com.myretail.myretailservicewebapp.endpoints

import com.myretail.myretailservicewebapp.domain.Product
import com.myretail.myretailservicewebapp.service.MyRetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = 'products')
class MyRetailResource {

    @Autowired
    MyRetailService myRetailService

    @GetMapping(path = '/{id}', produces = 'application/json')
    @ResponseBody
    Product getProductDetails(
            @PathVariable('id') Long id
    ) {
        return myRetailService.getProductDetails(id)
    }
}