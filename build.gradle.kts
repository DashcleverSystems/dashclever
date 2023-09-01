import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.6"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    kotlin("plugin.jpa") version "1.7.22"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    id("org.springdoc.openapi-gradle-plugin") version "1.6.0"
    id("com.github.node-gradle.node") version "7.0.0"
}

group = "pl.dashclever"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

extra["testcontainersVersion"] = "1.18.0"

tasks.processResources.configure {
    dependsOn(copyWebBuildToResources)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("com.itextpdf:itextpdf:5.5.13.3")
    implementation("org.liquibase:liquibase-core:4.21.1")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
    implementation("io.github.oshai:kotlin-logging-jvm:5.0.1")

    compileOnly("org.projectlombok:lombok:1.18.26")

    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor("org.projectlombok:lombok:1.18.26")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.rest-assured:rest-assured:5.3.0")
    testImplementation("io.rest-assured:kotlin-extensions:5.3.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("io.mockk:mockk:1.13.5")

    testCompileOnly("org.projectlombok:lombok:1.18.26")

    testAnnotationProcessor("org.projectlombok:lombok:1.18.26")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

val stage = tasks.register("stage") {
    dependsOn(tasks.bootJar)
}

val dockerComposeFile = "./docker/docker-compose.yaml"
val dockerComposeAppVolumeName = "dashclever-volume"

val setDev = tasks.register<Exec>("setDev") {
    commandLine("docker", "compose", "-p", project.name, "-f", dockerComposeFile, "up", "-d", "--build", "--remove-orphans")
    doLast {
        tasks.bootRun.configure {
            systemProperty("jdbc.db.url", "jdbc:postgresql://localhost:5432/dashclever")
            systemProperty("jdbc.db.username", "postgres")
            systemProperty("jdbc.db.password", "postgres")
            systemProperty("spring.security.logging", "TRACE")
        }
    }
}

val bootDev = tasks.register("bootDev") {
    dependsOn(tasks.bootRun, setDev)
    tasks.getByName("bootRun").mustRunAfter(tasks.getByName("setDev"))
}

val stopDev = tasks.register<Exec>("stopDev") {
    commandLine("docker", "compose", "-p", project.name, "-f", dockerComposeFile, "stop")
}

val cleanDev = tasks.register<Exec>("cleanDev") {
    dependsOn(tasks.clean)
    commandLine("docker", "compose", "-p", project.name, "-f", dockerComposeFile, "down", "--volumes", "--remove-orphans")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

detekt {
    config.setFrom("detekt-config.yml")
}

val copyWebBuildToResources = tasks.register<Copy>("copyWebBuildToResources") {
    dependsOn("web:buildWeb")
    from("web/dist")
    destinationDir = fileTree("src/main/resources/public").dir
}

tasks.clean.configure {
    dependsOn(cleanWebBuild)
}

val cleanWebBuild = tasks.register<Delete>("cleanDist") {
    delete = setOf("web/dist", "src/main/resources/public")
}
