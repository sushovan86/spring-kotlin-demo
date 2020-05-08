package com.chak.sc.routers

import arrow.core.Either.Left
import arrow.core.Either.Right
import com.chak.sc.ext.contentType
import com.chak.sc.ext.created
import com.chak.sc.repo.MessageRepository
import com.chak.sc.services.CustomerService
import kotlinx.coroutines.FlowPreview
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok

class CustomerHandler(private val customerService: CustomerService,
                      private val messageRepository: MessageRepository) {

    suspend fun all(serverRequest: ServerRequest) =
            ok().contentType(serverRequest)
                    .bodyAndAwait(customerService.findAll())

    suspend fun firstNCustomers(serverRequest: ServerRequest): ServerResponse {

        messageRepository.sendLog(serverRequest)

        return when (val customers = customerService
                .findFirstNCustomers(serverRequest.pathVariable(URI_PARAM_COUNT))) {

            is Right -> ok().contentType(serverRequest).bodyAndAwait(customers.b)
            is Left -> customers.a.returnResponse()
        }
    }

    suspend fun findById(serverRequest: ServerRequest) =
            when (val customer = customerService
                    .findByCustomerId(serverRequest.pathVariable(URI_PARAM_ID))) {

                is Right -> ok().contentType(serverRequest).bodyValueAndAwait(customer.b)
                is Left -> customer.a.returnResponse()
            }

    suspend fun findByCustomerNumber(serverRequest: ServerRequest) =
            when (val customer = customerService
                    .findByCustomer(serverRequest.pathVariable(URI_PARAM_CUSTOMER_NUMBER))) {

                is Right -> ok().contentType(serverRequest).bodyValueAndAwait(customer.b)
                is Left -> customer.a.returnResponse()
            }

    suspend fun createCustomer(serverRequest: ServerRequest): ServerResponse {

        messageRepository.sendLog(serverRequest)

        return when (val customerOutput = customerService
                .createCustomer(serverRequest.awaitBodyOrNull())) {

            is Right -> created(serverRequest,
                    "/customer/id/{$URI_PARAM_ID}",
                    customerOutput.b.id)
                    .contentType(serverRequest)
                    .bodyValueAndAwait(customerOutput.b)
            is Left -> customerOutput.a.returnResponse()
        }
    }

    suspend fun updateCustomer(serverRequest: ServerRequest) =
            when (val customerOutput = customerService
                    .updateCustomer(serverRequest.awaitBodyOrNull())) {

                is Right -> ok().contentType(serverRequest).bodyValueAndAwait(customerOutput.b)
                is Left -> customerOutput.a.returnResponse()
            }

    suspend fun deleteByCustomerId(serverRequest: ServerRequest) =
            when (val customerOutput = customerService
                    .deleteByCustomerId(serverRequest.pathVariable(URI_PARAM_ID))) {

                is Right -> ok().contentType(serverRequest).bodyValueAndAwait(customerOutput.b)
                is Left -> customerOutput.a.returnResponse()
            }

    @FlowPreview
    suspend fun streamMessages(serverRequest: ServerRequest) =
            ok().sse().bodyAndAwait(messageRepository.stream())

    suspend fun streamHighPricedProducts(serverRequest: ServerRequest) =
            when (val productOutput = customerService
                    .productsWithPricesGreaterThan(serverRequest.pathVariable(URI_PARAM_PRICE))) {

                is Right -> ok().contentType(serverRequest).bodyAndAwait(productOutput.b)
                is Left -> productOutput.a.returnResponse()
            }
}