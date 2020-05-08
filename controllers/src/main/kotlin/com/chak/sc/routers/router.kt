package com.chak.sc.routers

import kotlinx.coroutines.FlowPreview
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

const val URI_PARAM_ID = "id"
const val URI_PARAM_COUNT = "count"
const val URI_PARAM_PRICE = "price"
const val URI_PARAM_CUSTOMER_NUMBER = "customerNumber"

@FlowPreview
fun applicationRouter(customerHandler: CustomerHandler) = coRouter {

    GET("/messages", customerHandler::streamMessages)

    "/customer".nest {
        GET("/all", customerHandler::all)
        GET("/id/{$URI_PARAM_ID}", customerHandler::findById)
        GET("/first/{$URI_PARAM_COUNT}", customerHandler::firstNCustomers)
        GET("/findByNumber/{$URI_PARAM_CUSTOMER_NUMBER}", customerHandler::findByCustomerNumber)

        DELETE("/{$URI_PARAM_ID}", customerHandler::deleteByCustomerId)

        PUT("/update", customerHandler::updateCustomer)

        POST("/create", customerHandler::createCustomer)
    }

    "/product".nest {
        GET("/priceGreater/{$URI_PARAM_PRICE}", customerHandler::streamHighPricedProducts)
    }

}.andOther(errorRouter())

fun errorRouter() =
        RouterFunctions.route(
                RequestPredicates.all(),
                HandlerFunction {
                    ServerResponse
                            .status(HttpStatus.BAD_REQUEST)
                            .body(Mono.just("No resource available @ ${it.uri()}"))
                }
        )
