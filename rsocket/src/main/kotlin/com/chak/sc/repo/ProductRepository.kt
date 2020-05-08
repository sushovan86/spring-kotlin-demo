package com.chak.sc.repo

import com.chak.sc.model.Product
import com.chak.sc.model.ProductInventory
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.flow
import org.springframework.data.r2dbc.query.Criteria
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(private val databaseClient: DatabaseClient) {

    fun productsWithPriceHigherThan(price: Double) = databaseClient.select()
            .from(Product::class.java)
            .matching(Criteria.where("price").greaterThan(price))
            .fetch()
            .flow()

    fun productInventoryByProductId(productId: Int) = databaseClient.select()
            .from(ProductInventory::class.java)
            .matching(Criteria.where("productid").`is`(productId))
            .fetch()
            .flow()

}