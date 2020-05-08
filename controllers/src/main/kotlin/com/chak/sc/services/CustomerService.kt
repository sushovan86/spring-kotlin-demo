package com.chak.sc.services

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.chak.sc.ext.Error
import com.chak.sc.ext.Error.*
import com.chak.sc.model.Customer
import com.chak.sc.repo.CustomerRepository
import com.chak.sc.repo.MessageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.springframework.stereotype.Service

@Service
class CustomerService(private val customerRepository: CustomerRepository,
                      private val messageRepository: MessageRepository) {

    fun findAll() = customerRepository.findAll()

    @ExperimentalCoroutinesApi
    fun findFirstNCustomers(countParam: String?): Either<Error, Flow<Customer>> =
            countParam?.toIntOrNull()?.let {

                if (it < 1)
                    BadRequest("Invalid count value $it. It must be >= 1").left()
                else {

                    customerRepository.firstNRecords(it)
                            .onEach { customer ->
                                println("Fetch for $customer")
                                populateAddressDetails(customer)
                            }.right()
                }

            } ?: BadRequest("Parameter count is not a valid integer").left()

    private suspend fun populateAddressDetails(customer: Customer): Customer {
        customerRepository.findAddressByCustomerId(customer.id!!)
                .toList(customer.addressList)
        return customer
    }

    suspend fun findByCustomer(customerNumber: String?): Either<Error, Customer> =
            customerNumber?.let {
                when (val customer = customerRepository.findCustomerByCustomerNumber(it)) {
                    null -> NotFound("Customer record not found").left()
                    else -> populateAddressDetails(customer).right()
                }
            } ?: BadRequest("CustomerNumber input is empty").left()

    suspend fun createCustomer(customer: Customer?): Either<Error, Customer> =
            customer?.let {
                when (val insertedCustomer = customerRepository.create(it)) {
                    null -> ServerError("Cannot create customer").left()
                    else -> insertedCustomer.right()
                }
            } ?: BadRequest("Customer input is invalid").left()

    suspend fun updateCustomer(customer: Customer?): Either<Error, Int> =
            customer?.let {
                customerRepository.update(it).right()
            } ?: BadRequest("Customer input is invalid").left()

    suspend fun deleteByCustomerId(id: String?): Either<Error, Int> =
            id?.toIntOrNull()?.let {
                customerRepository.delete(it).right()
            } ?: BadRequest("id is invalid").left()

    suspend fun findByCustomerId(id: String?): Either<Error, Customer> =
            id?.toIntOrNull()?.let {
                when (val customer = customerRepository.findById(it)) {
                    null -> NotFound("Customer record not found").left()
                    else -> populateAddressDetails(customer).right()
                }
            } ?: BadRequest("CustomerNumber input is empty").left()

    suspend fun productsWithPricesGreaterThan(price: String?) =
            price?.toDoubleOrNull()?.let {
                messageRepository.productsWithPricesGreaterThan(it).right()
            } ?: BadRequest("Invalid Price value $price").left()
}