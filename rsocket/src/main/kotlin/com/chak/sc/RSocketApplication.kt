package com.chak.sc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RSocketApplication

fun main(args: Array<String>) {
    runApplication<RSocketApplication>(*args)
}