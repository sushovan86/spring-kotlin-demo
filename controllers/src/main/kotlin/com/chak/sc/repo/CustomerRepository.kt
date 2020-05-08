package com.chak.sc.repo

import com.chak.sc.model.Address
import com.chak.sc.model.Customer
import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort.by
import org.springframework.data.r2dbc.core.*
import org.springframework.data.r2dbc.query.Criteria
import org.springframework.stereotype.Repository

@Repository
class CustomerRepository(private val databaseClient: DatabaseClient) {

    val sortByCustomerId = by("id").ascending()

    suspend fun create(customer: Customer) = databaseClient.insert()
            .into(Customer::class.java)
            .using(customer)
            .map { row ->
                customer.id = row["id"] as Int
                customer
            }
            .awaitOneOrNull()

    suspend fun update(customer: Customer) = databaseClient.update()
            .table(Customer::class.java)
            .using(customer)
            .fetch()
            .awaitRowsUpdated()

    suspend fun delete(id: Int) = databaseClient.delete()
            .from(Customer::class.java)
            .matching(Criteria.where("id").`is`(id))
            .fetch()
            .awaitRowsUpdated()

    fun findAll() = databaseClient.select()
            .from(Customer::class.java)
            .orderBy(sortByCustomerId)
            .fetch()
            .flow()

    suspend fun findById(id:Int) = databaseClient
            .select()
            .from(Customer::class.java)
            .matching(Criteria.where("id").`is`(id))
            .fetch()
            .awaitFirstOrNull()

    fun firstNRecords(count: Int) = databaseClient.select()
            .from(Customer::class.java)
            .page(PageRequest.of(0, count, sortByCustomerId))
            .fetch()
            .flow()

    fun findAddressByCustomerId(id:Int) = databaseClient.select()
            .from(Address::class.java)
            .matching(Criteria.where("customerid").`is`(id))
            .fetch()
            .flow()

    suspend fun findCustomerByCustomerNumber(customerNumber: String) = databaseClient
            .select()
            .from(Customer::class.java)
            .matching(Criteria.where("customerNumber").`is`(customerNumber))
            .fetch()
            .awaitFirstOrNull()
}