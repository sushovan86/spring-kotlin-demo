package com.chak.sc.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("product")
data class Product(@Id var id: Int? = null,
                   @Column("productdescription") val productDescription: String,
                   @Column("company") val company: String,
                   @Column("price") val price: Double) {

    @Transient
    val inventoryList = mutableListOf<ProductInventory>()
}

@Table("productinventory")
data class ProductInventory(@Id var id: Int? = null,
                            @Column("productid") val productId: Int,
                            @Column("inventorycode") val inventoryCode: String,
                            @Column("datewhenadded") val dateWhenAdded: LocalDate,
                            @Column("status") val status: String)