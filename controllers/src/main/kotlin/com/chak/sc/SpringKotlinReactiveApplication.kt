package com.chak.sc

import com.chak.sc.routers.CustomerHandler
import com.chak.sc.routers.applicationRouter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans

@SpringBootApplication
class SpringKotlinRsocketApplication

@FlowPreview
@ExperimentalCoroutinesApi
fun main(args: Array<String>) {
    runApplication<SpringKotlinRsocketApplication>(*args) {
        addInitializers(beanDefinition())
    }
}

@FlowPreview
private fun beanDefinition() = beans {
    bean<CustomerHandler>()
    bean {
        applicationRouter(ref())
    }
}