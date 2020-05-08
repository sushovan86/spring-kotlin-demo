package com.chak.sc.repo

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.connectTcpAndAwait
import org.springframework.stereotype.Component

@Component
class RSocketConnection(private val rsocketBuilder: RSocketRequester.Builder,
                        @Value("\${application.rsocket.host}") private val host: String,
                        @Value("\${application.rsocket.port}") private val port: Int) {

    private var requester: RSocketRequester? = null

    @Synchronized
    private suspend fun createConnection() = rsocketBuilder
            .dataMimeType(MediaType.APPLICATION_CBOR)
            .connectTcpAndAwait(host, port)
            .also {
                requester = it
            }

    suspend fun connect() = requester ?: createConnection()
}