import BuildOptions.Plugins
import BuildOptions.kotlin

apply(plugin = Plugins.SPRING_BOOT)
apply(plugin = Plugins.SPRING_DEP_MGMT)
apply(plugin = kotlin(Plugins.KOTLIN_SPRING))

dependencies {
    val implementation by configurations
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-rsocket")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    val runtimeOnly by configurations
    runtimeOnly("io.r2dbc:r2dbc-postgresql")
    runtimeOnly("org.postgresql:postgresql")

    val testImplementation by configurations
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test")
}