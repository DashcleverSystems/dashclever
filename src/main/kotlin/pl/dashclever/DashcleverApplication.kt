package pl.dashclever

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
