import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
	kotlin("jvm") version "1.6.10"
	kotlin("kapt") version "1.4.21"
	kotlin("plugin.spring") version "1.4.21"
	kotlin("plugin.jpa") version "1.3.41"
	kotlin("plugin.noarg") version "1.3.41"
	kotlin("plugin.allopen") version "1.3.72"

	jacoco
	id("org.springframework.boot") version "2.4.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.flywaydb.flyway") version "5.2.4"
	id("org.jetbrains.dokka") version "0.9.18"
	id("org.asciidoctor.convert") version "2.2.0"
	id("com.gorylenko.gradle-git-properties") version "2.3.2"
}

group = "com.japanet"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven {
		url = uri("https://packages.confluent.io/maven")
	}
}


val springfoxVersion = "2.9.2"
val feignHttpClientVersion = "10.2.0"
val kotlinLoggingVersion = "1.6.22"
val commonsLangVersion = "3.8.1"
val mapstructVersion = "1.4.1.Final"
val trimouVersion = "2.5.0.Final"
val springValidator = "6.0.1.Final"
val mysqlConnectorVersion = "8.0.33"
val springCloudVersion = "2020.0.0"
val springCloudStreamVersion = "3.1.0"
val awaitilityVersion = "3.1.6"
val kafkaAvroSerializerVersion = "5.1.0"
val avroVersion = "1.8.2"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.springfox:springfox-swagger2:$springfoxVersion")
	implementation("io.springfox:springfox-swagger-ui:$springfoxVersion")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-json")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-undertow")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.cloud:spring-cloud-starter-config")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
	implementation("io.github.openfeign:feign-httpclient:$feignHttpClientVersion")
	implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
	implementation("org.apache.commons:commons-lang3:$commonsLangVersion")
	implementation("org.mapstruct:mapstruct:$mapstructVersion")
	implementation("org.trimou:trimou-core:$trimouVersion")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.json:json:20201115")
	implementation("org.hibernate:hibernate-validator:$springValidator")
	implementation("mysql:mysql-connector-java:$mysqlConnectorVersion")
	implementation("org.apache.avro:avro:$avroVersion")
	kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")

	runtimeOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.h2database:h2")
	testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.awaitility:awaitility-kotlin:$awaitilityVersion")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
	}
}

kapt {
	useBuildCache = true
	arguments {
		arg("mapstruct.defaultComponentModel", "spring")
	}
}

allOpen {
	annotation("javax.persistence.Entity")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

val exclusions = listOf("**/CustomerapiApplication",
	"com/japanet/menuapi/config/**",
	"com/japanet/menuapi/controller/response/**",
	"com/japanet/menuapi/controller/advice/**",
	"com/japanet/menuapi/dto/**",
	"com/japanet/menuapi/entity/**",
	"com/japanet/menuapi/enums/**",
	"com/japanet/menuapi/exception/**",
	"com/japanet/menuapi/mapper/**",
	"com/japanet/menuapi/repository/**",
	"com/japanet/menuapi/utils/**")

tasks.withType<Test> {
	addTestListener(object: TestListener {
		override fun beforeTest(testDescriptor: TestDescriptor?) {}
		override fun afterSuite(suite: TestDescriptor?, result: TestResult?) {}
		override fun beforeSuite(suite: TestDescriptor?) {}
		override fun afterTest(testDescriptor: TestDescriptor?, result: TestResult?) {
			logger.lifecycle("$testDescriptor")
			logger.lifecycle("Result: $result \n")
		}
	})
	minHeapSize = "1g"
	maxHeapSize = "2g"
}

afterEvaluate {
	tasks {
		jacoco {
			toolVersion = "0.8.8"
		}

		jacocoTestReport {
			reports {
				html.isEnabled = true
			}
			applyExclusions(classDirectories)
		}

		jacocoTestCoverageVerification {
			violationRules {
				rule {
					element = "BUNDLE"
					limit {
						counter = "INSTRUCTION"
						value = "COVEREDRATIO"
						minimum = BigDecimal.valueOf(0.8)
					}
				}
			}
			applyExclusions(classDirectories)
			dependsOn(jacocoTestReport)
		}
		test {
			finalizedBy(jacocoTestCoverageVerification)
		}
	}
}

fun applyExclusions(collection: ConfigurableFileCollection) {
	collection.setFrom(collection.files.flatMap {
		fileTree(it) {
			exclude(exclusions)
		}
	})
}

allprojects {
	springBoot.buildInfo()
	tasks.getByName("bootBuildInfo").mustRunAfter(tasks.getByName("processResources"))
}
