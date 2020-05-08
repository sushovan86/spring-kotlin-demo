package com.chak.sc.repo

import com.chak.sc.model.Message
import com.chak.sc.model.Product
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.reactive.asFlow
import org.springframework.messaging.rsocket.dataWithType
import org.springframework.messaging.rsocket.retrieveFlow
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.ReplayProcessor
import java.time.LocalDateTime

@Repository
class MessageRepository(private val rsocketConnection: RSocketConnection) {

    private val processor = ReplayProcessor.create<Message>(0)

    private val sink = processor.sink()

    suspend fun sendLog(serverRequest: ServerRequest) {
        val message = Message(method = serverRequest.methodName(), uri = serverRequest.uri().toString())
        sink.next(message)
    }

    @FlowPreview
    suspend fun stream(): Flow<Message> {

        val requester = rsocketConnection.connect()

        val replies = requester
                .route("log.messages")
                .dataWithType(processor)
                .retrieveFlow<Message>()

        val broadcasts = requester
                .route("bot.message")
                .retrieveFlow<Message>()

        return flowOf(processor.asFlow(), replies, broadcasts).flattenMerge()
    }

    suspend fun productsWithPricesGreaterThan(price: Double) = rsocketConnection.connect()
            .route("products.prices")
            .data(price)
            .retrieveFlow<Product>()
            .onEach {
                println("${LocalDateTime.now()} CONTROLLER: Received product [id=${it.id}, " +
                        "description = ${it.productDescription}, price = ${it.price}]")
            }
}