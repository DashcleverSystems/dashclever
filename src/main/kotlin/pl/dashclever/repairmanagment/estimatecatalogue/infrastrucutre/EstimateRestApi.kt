package pl.dashclever.repairmanagment.estimatecatalogue.infrastrucutre

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.Explode.TRUE
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.tags.Tag
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
import pl.dashclever.repairmanagment.estimatecatalogue.Estimate
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateSpecifications
import java.net.URI
import java.time.LocalDateTime
import java.util.UUID

private const val PATH = "/api/estimatecatalogue"

@RestController
@RequestMapping(PATH)
@Tag(name = "estimate-api")
internal class EstimateRestApi(
    private val estimateRepository: EstimateRepository
) {

    @PostMapping
    fun create(
        @Valid @RequestBody
        estimate: Estimate
    ): ResponseEntity<Estimate> {
        if (this.estimateRepository.existsByEstimateId(estimate.estimateId)) {
            throw DomainException(ALREADY_EXISTS)
        }
        this.estimateRepository.save(estimate)
        return ResponseEntity
            .created(URI.create("$PATH/${estimate.id}"))
            .build()
    }


    data class EstimateFilters(
        val estimateId: String? = null,
        val createdAfter: LocalDateTime? = null,
        val pageNo: Int = 0,
        val pageSize: Int = 20,
        val sortDirection: SortDirection = DESC
    )

    @GetMapping(produces = ["application/json"])
    fun get(@Parameter(explode = TRUE, `in` = ParameterIn.QUERY) filters: EstimateFilters): Page<Estimate> {
        var specification: Specification<Estimate>? = null
        if (filters.createdAfter != null) {
            specification = EstimateSpecifications.createdOnAfter(filters.createdAfter)
        }
        if (filters.estimateId != null) {
            specification = specification?.and(EstimateSpecifications.estimateId(filters.estimateId))
                ?: EstimateSpecifications.estimateId(filters.estimateId)
        }

        val sort = when (filters.sortDirection) {
            ASC -> Sort.by("createdOn").ascending()
            DESC -> Sort.by("createdOn").descending()
        }
        val pageReq = PageRequest.of(filters.pageNo, filters.pageSize, sort)
        return specification?.let { this.estimateRepository.findAll(it, pageReq) } ?: this.estimateRepository.findAll(
            pageReq
        )
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    fun delete(@RequestParam estimateId: UUID) {
        return this.estimateRepository.deleteById(estimateId)
    }
}
