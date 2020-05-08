package com.chak.sc.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table
data class Customer(@Id var id: Int? = null,
                    @Column("customernumber") val customerNumber: String,
                    @Column("firstName") val firstName: String?,
                    @Column("surname") val surname: String?,
                    @Column("userName") val userName: String?,
                    @Column("dob") val dob: LocalDate?) {

    @Transient
    val addressList = mutableListOf<Address>()
}

@Table("address")
data class Address(@Id var id: Int? = null,
                   @Column("customerid") val customerId: Int,
                   @Column("addresstype") val addressType: String,
                   @Column("addressline1") val addressLine1: String?,
                   @Column("addressline2") val addressLine2: String?,
                   @Column("addressline3") val addressLine3: String?,
                   @Column("state") val state: String?,
                   @Column("postcode") val postCode: String,
                   @Column("country") val country: String
)