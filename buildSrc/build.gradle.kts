group = "com.chak.sc"
version = "1.0"

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation(kotlin("script-runtime"))
}