package com.chak.sc.controller

import com.chak.sc.model.Message
import com.chak.sc.repo.ProductRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import java.time.LocalDateTime
import java.util.*

@Controller
class MessageController(private val productRepository: ProductRepository) {

    @FlowPreview
    @MessageMapping("log.messages")
    fun incomingMessages(messages: Flow<Message>) = messages.flatMapMerge {
        it.id = UUID.randomUUID()
        flow {
            println("Message consumed = $it")
            emit(it)
        }
    }

    @MessageMapping("bot.message")
    fun broadcastRequestLogged() = flow {
        while (true) {
            emit(Message(id = UUID.randomUUID(), method = "rsocket", uri = "bot.message"))
            delay(5000)
        }
    }

    @FlowPreview
    @MessageMapping("products.prices")
    fun fetchProductInfoForPriceGreaterThan(price: Double) =
            productRepository.productsWithPriceHigherThan(price)
                    .onEach {
                        println("${LocalDateTime.now()} RSOCKET: Fetch inventory for product $it")
                        productRepository.productInventoryByProductId(it.id!!)
                                .toList(it.inventoryList)
                    }
}