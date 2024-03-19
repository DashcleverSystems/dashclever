package pl.dashclever

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
internal class HealthCheck {

    private companion object {

        const val HEALTH_RESP = "UP AND RUNNING"
    }

    @GetMapping("/health")
    fun health() = HEALTH_RESP
}
