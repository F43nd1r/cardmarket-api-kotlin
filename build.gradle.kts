import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    `maven-publish`
    id("net.researchgate.release") version "2.8.1"
    id("com.jfrog.bintray") version "1.8.4"
}

group = "com.faendir"

repositories {
    jcenter()
    mavenCentral()
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
    testImplementation("org.hamcrest:hamcrest-all:1.3")
    testImplementation("com.natpryce:hamkrest:1.7.0.0")
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

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

 tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val publication = publishing.publications.create("maven", MavenPublication::class) {
    from(components["kotlin"])
    artifact(tasks["sourcesJar"])
    pom {
        groupId = project.group.toString()
        artifactId = project.name
        name.set(project.name)
        description.set("Kotlin API for cardmarket.com")
        url.set("https://github.com/F43nd1r/cardmarket-api-kotlin")
        licenses {
            license {
                name.set("Apache-2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("F43nd1r")
                name.set("Lukas Morawietz")
                email.set("support@faendir.com")
            }
        }
        scm {
            connection.set("scm:git:git@github.com:F43nd1r/cardmarket-api-kotlin.git")
            developerConnection.set("scm:git:git@github.com:F43nd1r/cardmarket-api-kotlin.git")
            url.set("https://github.com/F43nd1r/cardmarket-api-kotlin.git")
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_API_KEY")
    setPublications(publication.name)
    publish = true
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "maven"
        val pom = publication.pom
        name = pom.name.get()
        websiteUrl = pom.url.get()
        vcsUrl = pom.url.get()
        setLicenses("Apache-2.0")
    })
}

release {
    tagTemplate = "v\$version"
    buildTasks = listOf("assemble")
}

tasks["afterReleaseBuild"].dependsOn(tasks["bintrayUpload"])