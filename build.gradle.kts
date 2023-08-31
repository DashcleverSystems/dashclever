import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.npm.task.NpxTask
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

tasks.register("stage") {
    dependsOn("npmInstallWeb", "copyDist", tasks.bootJar)
}

val dockerComposeFile = "./docker/docker-compose.yaml"
val dockerComposeAppVolumeName = "app-db-volume"
val dockerComposeKeycloakVolumeName = "keycloak-db-volume"

tasks.register<Exec>("setDev") {
    commandLine("docker", "compose", "-p", project.name, "-f", dockerComposeFile, "up", "-d", "--build", "--remove-orphans")
    doLast {
        tasks.bootRun.configure {
            systemProperty("jdbc.db.url", "jdbc:postgresql://localhost:5432/app-db")
            systemProperty("jdbc.db.username", "postgres")
            systemProperty("jdbc.db.password", "postgres")
            systemProperty("app.jwt.issuer", "http://localhost")
            systemProperty("app.jwt.secret", "uberSecretJwt")
            systemProperty("spring.security.logging", "TRACE")
        }
    }
}

tasks.register("bootDev") {
    dependsOn("bootRun", "setDev")
    tasks.getByName("bootRun").mustRunAfter(tasks.getByName("setDev"))
}

tasks.register<Exec>("stopDev") {
    commandLine("docker", "compose", "-p", project.name, "-f", dockerComposeFile, "stop")
}

tasks.register<Exec>("cleanDev") {
    commandLine("docker", "compose", "-p", project.name, "-f", dockerComposeFile, "down", "--volumes", "--remove-orphans")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

detekt {
    config.setFrom("detekt-config.yml")
}

tasks.register<Exec>("copyDist") {
    dependsOn("buildWeb")
    commandLine("cp", "-r", "web/dist", "src/main/resources/public")
}
tasks.register<NpmTask>("npmInstallWeb"){
    this.workingDir.set(project.fileTree("web").dir)
    args.set(listOf("install", "--legacy-peer-deps"))
}

tasks.register<NpmTask>("buildWeb") {
    dependsOn("npmInstallWeb")
    workingDir.set(project.fileTree("web").dir)
    args.set(listOf("run", "build"))
}
