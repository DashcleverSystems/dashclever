package pl.dashclever.repairmanagment.estimatecatalogue

import jakarta.validation.Valid
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.net.URI
import java.util.UUID

private const val PATH = "/api/estimatecatalogue"

@RestController
@RequestMapping(PATH)
internal class EstimateController(
    private val estimateRepository: EstimateRepository,
) {

    @PostMapping
    fun create(@Valid @RequestBody estimate: Estimate): ResponseEntity<Estimate> {
        this.estimateRepository.save(estimate)
        return ResponseEntity
            .created(URI.create("$PATH/${estimate.id}"))
            .build()
    }

    @GetMapping
    fun get(@RequestParam(required = false) estimateId: String?): List<Estimate> {
        if (estimateId == null)
            return this.estimateRepository.findAll()
        return listOf(
            this.estimateRepository.findByEstimateId(estimateId)
                .orElseThrow { ResponseStatusException(NOT_FOUND) }
        )
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    fun delete(@RequestParam estimateId: String) {
        return this.estimateRepository.deleteById(UUID.fromString(estimateId))
    }
}
