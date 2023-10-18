package pl.dashclever.repairmanagment.estimatecatalogue

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
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
import pl.dashclever.publishedlanguage.ALREADY_EXISTS
import pl.dashclever.publishedlanguage.DomainException
import pl.dashclever.publishedlanguage.SortDirection
import pl.dashclever.publishedlanguage.SortDirection.ASC
import pl.dashclever.publishedlanguage.SortDirection.DESC
import java.net.URI
import java.time.LocalDateTime
import java.util.UUID

private const val PATH = "/api/estimatecatalogue"

@RestController
@RequestMapping(PATH)
internal class EstimateController(
    private val estimateRepository: EstimateRepository,
) {

    @PostMapping
    fun create(@Valid @RequestBody estimate: Estimate): ResponseEntity<Estimate> {
        if (this.estimateRepository.existsByEstimateId(estimate.estimateId)) {
            throw DomainException(ALREADY_EXISTS)
        }
        this.estimateRepository.save(estimate)
        return ResponseEntity
            .created(URI.create("$PATH/${estimate.id}"))
            .build()
    }

    @GetMapping
    fun get(
        @RequestParam(required = false) estimateId: String?,
        @RequestParam(required = false) createdAfter: LocalDateTime?,
        @RequestParam(required = false, defaultValue = "0") pageNo: Int,
        @RequestParam(required = false, defaultValue = "20") pageSize: Int,
        @RequestParam(required = false, defaultValue = "DESC") sortDirection: SortDirection
    ): Page<Estimate> {
        var specification: Specification<Estimate>? = null
        if (createdAfter != null)
            specification = EstimateSpecifications.createdOnAfter(createdAfter)
        if (estimateId != null)
            specification = specification?.and(EstimateSpecifications.estimateId(estimateId)) ?: EstimateSpecifications.estimateId(estimateId)

        val sort = when (sortDirection) {
            ASC -> Sort.by("createdOn").ascending()
            DESC -> Sort.by("createdOn").descending()
        }
        val pageReq = PageRequest.of(pageNo, pageSize, sort)
        return this.estimateRepository.findAll(specification, pageReq)
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    fun delete(@RequestParam estimateId: String) {
        return this.estimateRepository.deleteById(UUID.fromString(estimateId))
    }
}
