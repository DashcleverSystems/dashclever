package pl.dashclever

import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import pl.dashclever.spring.security.keycloak.KeycloakServerProperties

private val logger = KotlinLogging.logger { }

@OpenAPIDefinition(
    servers = [
        Server(
            url = "https://dashclever-c2650ed3f0d3.herokuapp.com",
            description = "Development Server"
        ),
        Server(
            url = "http://localhost:9999",
            description = "Local server"
        )
    ]
)
@SpringBootApplication
class DashcleverApplication {

    @Bean
    fun onApplicationReadyEventListener(
        serverProperties: ServerProperties,
        keycloakServerProperties: KeycloakServerProperties
    ): ApplicationListener<ApplicationReadyEvent> {
        return ApplicationListener<ApplicationReadyEvent> { evt: ApplicationReadyEvent? ->
            val port = serverProperties.port
            val keycloakContextPath = keycloakServerProperties.contextPath
            logger.info { "Embedded Keycloak started: http://localhost:{$port}{$keycloakContextPath} to use keycloak" }
        }
    }
}

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<DashcleverApplication>(*args)
}

