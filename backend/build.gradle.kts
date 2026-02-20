import java.util.Locale

plugins {
    java
    jacoco
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.example.atm"
version = "0.0.1-SNAPSHOT"
description = "atm-backend"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    compileOnly("org.projectlombok:lombok")
//    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.13"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.80".toBigDecimal()
            }
            excludes = listOf(
                "ru.example.atm.AtmBackendApplication"
            )
        }
    }
}

tasks.register("printCoverage") {
    group = "verification"
    description = "Runs tests, builds JaCoCo report and prints total LINE coverage percent."
    dependsOn(tasks.jacocoTestReport)

    doLast {
        val reportFile = layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml").get().asFile
        if (!reportFile.exists()) {
            throw GradleException("JaCoCo report not found: ${reportFile.path}")
        }

        val regex = Regex("""<counter type="LINE" missed="(\d+)" covered="(\d+)"""")
        val match = regex.findAll(reportFile.readText()).lastOrNull()
            ?: throw GradleException("LINE counter not found in ${reportFile.path}")

        val missed = match.groupValues[1].toInt()
        val covered = match.groupValues[2].toInt()
        val total = covered + missed
        val percent = if (total == 0) 0.0 else covered.toDouble() * 100.0 / total.toDouble()

        println("LINE coverage: %.2f%% (covered=%d, missed=%d)".format(Locale.US, percent, covered, missed))
    }
}
