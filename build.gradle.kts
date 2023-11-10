import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    id("org.jlleitschuh.gradle.ktlint-idea") version "11.6.1"
    id("org.springdoc.openapi-gradle-plugin") version "1.8.0"
}

group = "pl.dashclever"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

extra["testcontainersVersion"] = "1.18.0"

tasks.processResources.configure {
    dependsOn(installWeb)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("org.apache.commons:commons-lang3:3.13.0")
    implementation("com.itextpdf:itextpdf:5.5.13.3")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")

    compileOnly("org.projectlombok:lombok")

    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.rest-assured:rest-assured:5.3.2")
    testImplementation("io.rest-assured:kotlin-extensions:5.3.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("io.mockk:mockk:1.13.8")

    testCompileOnly("org.projectlombok:lombok")

    testAnnotationProcessor("org.projectlombok:lombok")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = "17"
    }
}

val stage = tasks.register("stage") {
    dependsOn(tasks.bootJar)
}

val dockerComposeFile = "./docker/docker-compose.yaml"
val dockerComposeAppVolumeName = "dashclever-volume"

val devSystemProperties = mapOf(
    "jdbc.db.url" to "jdbc:postgresql://localhost:5432/dashclever",
    "jdbc.db.username" to "postgres",
    "jdbc.db.password" to "postgres",
    "spring.security.logging" to "TRACE",
    "openapi.enabled" to "true"
)

val setDev = tasks.register<Exec>("setDev") {
    commandLine("docker", "compose", "-p", project.name, "-f", dockerComposeFile, "up", "-d", "--build", "--remove-orphans")
    doLast {
        tasks.bootRun.configure {
            devSystemProperties.forEach { (property, value) ->
                systemProperty(property, value)
            }
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

val installWeb = tasks.register<Copy>("installWeb") {
    dependsOn("web:build")
    from("web/dist")
    destinationDir = fileTree("src/main/resources/public").dir
}

tasks.bootRunMainClassName.configure {
    dependsOn(setDev)
}

tasks.clean.configure {
    dependsOn(cleanWebBuild)
}

val cleanWebBuild = tasks.register<Delete>("cleanDist") {
    delete = setOf("web/dist", "src/main/resources/public")
}

val generateHttpClients = tasks.register("generateHttpClients") {
    dependsOn("web:generateHttpClients", tasks.generateOpenApiDocs)
    tasks.getByPath("web:generateHttpClients").mustRunAfter(tasks.getByName("generateOpenApiDocs"))
}

openApi {
    apiDocsUrl.set("http://localhost:9999/open-api")
    customBootRun {
        devSystemProperties.forEach { (property, value) ->
            this.systemProperties.put(property, value)
        }
    }
}
