plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-extension-annotations:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.0")
    implementation(kotlin("stdlib"))
}

application {
    applicationDefaultJvmArgs = listOf(
        "-Dotel.javaagent.debug=true",
        "-javaagent:libs/opentelemetry-javaagent.jar"
    )

    mainClass.set("Sandbox")
}