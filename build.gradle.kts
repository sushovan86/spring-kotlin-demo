plugins {
    id(BuildOptions.Plugins.SPRING_BOOT) version BuildOptions.Versions.SPRING_BOOT apply false
    id(BuildOptions.Plugins.SPRING_DEP_MGMT) version BuildOptions.Versions.SPRING_DEP_MGMT apply false
    kotlin(BuildOptions.Plugins.KOTLIN_JVM) apply false
    kotlin(BuildOptions.Plugins.KOTLIN_SPRING) version BuildOptions.Versions.KOTLIN_VERSION apply false
}

allprojects {

    group = "com.chak.sc"
    version = "1.0"

    repositories {
        maven {
            url = uri("https://repo.spring.io/milestone")
        }
        mavenCentral()
    }
}

subprojects {
    apply(plugin = BuildOptions.Plugins.CUSTOM_KOTLIN_CONF)
}