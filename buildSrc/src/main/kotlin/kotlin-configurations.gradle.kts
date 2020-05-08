import BuildOptions.KotlinDependencies
import BuildOptions.Plugins
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

apply(plugin = BuildOptions.kotlin(Plugins.KOTLIN_JVM))

dependencies {
    val implementation by configurations
    implementation(enforcedPlatform(KotlinDependencies.SPRING_DEP_BOM))
    implementation(KotlinDependencies.JDK8)
    implementation(KotlinDependencies.REFLECT)
    implementation(KotlinDependencies.COROUTINES)
    implementation(KotlinDependencies.JACKSON_MODULE)
    implementation(KotlinDependencies.R2DBC)
    implementation(KotlinDependencies.WEBFLUX)
    implementation(KotlinDependencies.ARROW)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
