package com.chak.sc.ext

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.util.UriComponentsBuilder

fun BodyBuilder.contentType(serverRequest: ServerRequest) =
        when (MediaType.TEXT_EVENT_STREAM) {
            in serverRequest.headers().accept() -> this.sse()
            else -> this.json()
        }

fun created(serverRequest: ServerRequest, path: String, vararg params: Any?): BodyBuilder {

    val requestUri = serverRequest.uri()
    val baseUri = "${requestUri.scheme}://${requestUri.host}:${requestUri.port}"
    val location = UriComponentsBuilder
            .fromUriString("$baseUri$path")
            .encode()
            .buildAndExpand(*params)
            .toUri()

    return created(location)
}

sealed class Error {

    data class NotFound(val errorReason: String = "") : Error() {
        override suspend fun returnResponse() = status(HttpStatus.NOT_FOUND)
                .bodyValueAndAwait(errorReason)
    }

    data class BadRequest(val errorReason: String = "") : Error() {
        override suspend fun returnResponse() = badRequest().bodyValueAndAwait(errorReason)
    }

    data class ServerError(val errorReason: String = "") : Error() {
        override suspend fun returnResponse() = status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValueAndAwait(errorReason)
    }

    abstract suspend fun returnResponse(): ServerResponse
}



