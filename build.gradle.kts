import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.adarshr.gradle.testlogger.theme.ThemeType


plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("io.gitlab.arturbosch.detekt") version "1.16.0"
    id("org.flywaydb.flyway") version "7.7.0"
    id("com.adarshr.test-logger") version "3.0.0"
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.spring") version "1.4.32"
    kotlin("plugin.jpa") version "1.5.0"
}

apply(plugin = "org.flywaydb.flyway")

group = "com"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_13

repositories {
    mavenCentral()
    jcenter {
        content {
            // just allow to include kotlinx projects
            // detekt needs 'kotlinx-html' for the html report
            includeGroup("org.jetbrains.kotlinx")
            includeGroup("org.amshove.kluent")
        }
    }
}

val detektVersion = "1.16.0"
val skrapeItVersion = "1.0.0-alpha8"
val wiremockVersion = "3.0.2"
val mockkVersion = "1.11.0"
val kotlinResultVersion = "1.1.11"
val kluentVersion = "1.65"
val textRazorVersion = "1.0.12"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("it.skrape:skrapeit-core:$skrapeItVersion")
    implementation("com.michael-bull.kotlin-result:kotlin-result:$kotlinResultVersion")
    implementation("com.textrazor:textrazor:$textRazorVersion")
    implementation("commons-validator:commons-validator:1.7")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock:$wiremockVersion")
    testImplementation("org.amshove.kluent:kluent:$kluentVersion")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
    detekt("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
    detekt("io.gitlab.arturbosch.detekt:detekt-cli:$detektVersion")
}

detekt {
    toolVersion = detektVersion
    input = files("./src")
    config = files("./detekt-config.yml")
    autoCorrect = true
}

tasks.register<Exec>("flywayCreate") {
    group = "flyway"
    description =
        "Creates a new migration by using the difference between JPA model in your current branch and model " +
            "created by migrations in main branch."
    run {
        standardInput = System.`in`
        standardOutput = System.`out`
    }
    workingDir("./db/scripts/")
    commandLine("./createMigration.sh")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}


tasks.test {
    useJUnitPlatform()

    testlogger {
        theme = ThemeType.STANDARD
    }
}
