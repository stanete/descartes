import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("io.gitlab.arturbosch.detekt") version "1.15.0"
	kotlin("jvm") version "1.4.21"
	kotlin("plugin.spring") version "1.4.21"
}

group = "com"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	jcenter {
		content {
			// just allow to include kotlinx projects
			// detekt needs 'kotlinx-html' for the html report
			includeGroup("org.jetbrains.kotlinx")
		}
	}
}

val detektVersion = "1.15.0"
val skrapeItVersion = "1.0.0-alpha8"
val wiremockVersion = "2.1.1.RELEASE"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("it.skrape:skrapeit-core:$skrapeItVersion")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock:$wiremockVersion")
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

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
