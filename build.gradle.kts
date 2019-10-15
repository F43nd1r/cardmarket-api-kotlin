import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.50"
}

group = "com.faendir"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    val jacksonVersion = "2.10.0"
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    val fuelVersion = "2.2.1"
    implementation("com.github.kittinunf.fuel:fuel:$fuelVersion")
    implementation("com.github.kittinunf.fuel:fuel-jackson:$fuelVersion")
    implementation("org.apache.logging.log4j:log4j-api:2.12.1")
    implementation("org.apache.logging.log4j:log4j-core:2.12.1")
    implementation("com.natpryce:konfig:1.6.10.0")
    implementation("org.redundent:kotlin-xml-builder:1.5.3")

    val junitVersion = "5.5.2"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events.add(TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.FULL
    }
    /*systemProperty("http.proxyHost", "localhost")
    systemProperty("http.proxyPort", "8888")
    systemProperty("https.proxyHost", "localhost")
    systemProperty("https.proxyPort", "8888")*/
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}