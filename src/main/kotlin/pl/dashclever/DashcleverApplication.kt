package pl.dashclever

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
class DashcleverApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<DashcleverApplication>(*args)
}

@RestController
@RequestMapping("/api")
internal class HealthCheck {

    private companion object {

        const val HEALTH_RESP = "UP AND RUNNING"
    }

    @GetMapping("/health")
    fun health() = HEALTH_RESP
}
