plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    id("com.diffplug.spotless") version "6.25.0"
    id("info.solidsoft.pitest") version "1.15.0"
    kotlin("plugin.serialization") version "2.0.0"
}

group = "com.atauchi"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mongodb")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("PASSED", "FAILED", "SKIPPED")
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

pitest {
    setProperty("junit5PluginVersion", "1.2.1")
    setProperty("pitestVersion", "1.15.2")
    setProperty("testPlugin", "junit5")
    setProperty("targetClasses", listOf("com.atauchi.transformerFileService.*"))
    setProperty("outputFormats", listOf("XML", "HTML"))
    setProperty("mutators", listOf("STRONGER"))
    setProperty("threads", Runtime.getRuntime().availableProcessors())
    setProperty("avoidCallsTo", listOf("kotlin.jvm.internal"))
    setProperty("mutationThreshold", 75)
    setProperty("coverageThreshold", 60)
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint("1.2.1")
    }

    kotlinGradle {
        target("*.gradle.kts")
        ktlint("1.2.1")
    }
}
