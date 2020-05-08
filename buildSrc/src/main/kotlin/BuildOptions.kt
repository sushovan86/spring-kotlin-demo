object BuildOptions {

    fun kotlin(module: String) = "org.jetbrains.kotlin.$module"
    fun dep(dependency: String) = ":$dependency"

    object Versions {
        const val KOTLIN_VERSION = "1.3.71"
        const val ARROW_VERSION = "0.10.4"

        const val SPRING_BOOT = "2.3.0.M3"
        const val SPRING_DEP_MGMT = "1.0.9.RELEASE"
    }

    object KotlinDependencies {
        const val REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN_VERSION}"
        const val JDK8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN_VERSION}"
        const val ARROW = "io.arrow-kt:arrow-core:${Versions.ARROW_VERSION}"

        const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-reactor"
        const val JACKSON_MODULE = "com.fasterxml.jackson.module:jackson-module-kotlin"
        const val R2DBC = "org.springframework.data:spring-data-r2dbc"
        const val WEBFLUX = "org.springframework:spring-webflux"

        const val SPRING_DEP_BOM = "org.springframework.boot:spring-boot-dependencies:${Versions.SPRING_BOOT}"
    }

    object Plugins {
        const val SPRING_BOOT = "org.springframework.boot"
        const val SPRING_DEP_MGMT = "io.spring.dependency-management"
        const val KOTLIN_JVM = "jvm"
        const val KOTLIN_SPRING = "plugin.spring"

        const val CUSTOM_KOTLIN_CONF = "kotlin-configurations"
        const val CUSTOM_SPRING_CONF = "spring-configurations"
    }

    object Subprojects {
        const val CORE = "core"
        const val CONTROLLERS = "controllers"
        const val RSOCKET = "rsocket"
    }
}