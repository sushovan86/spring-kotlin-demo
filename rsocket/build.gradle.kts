import BuildOptions.Plugins
import BuildOptions.Subprojects
import BuildOptions.dep

apply(plugin = Plugins.CUSTOM_SPRING_CONF)

val developmentOnly: Configuration by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {

    implementation(project(dep(Subprojects.CORE)))
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}