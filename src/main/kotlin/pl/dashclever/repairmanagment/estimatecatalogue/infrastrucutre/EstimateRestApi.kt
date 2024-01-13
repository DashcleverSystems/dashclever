package pl.dashclever.repairmanagment.estimatecatalogue.infrastrucutre

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.MediaType
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
import pl.dashclever.commons.exception.ALREADY_EXISTS
import pl.dashclever.commons.exception.SIZE_BETWEEN
import pl.dashclever.commons.paging.PageRequestDto
import pl.dashclever.commons.paging.SortDirection
import pl.dashclever.commons.paging.SortDirection.ASC
import pl.dashclever.commons.paging.SortDirection.DESC
import pl.dashclever.commons.time.LocalDateTimeHelper.asGmt
import pl.dashclever.repairmanagment.estimatecatalogue.Estimate
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository.EstimateSpecifications
import pl.dashclever.repairmanagment.estimatecatalogue.Job
import pl.dashclever.repairmanagment.estimatecatalogue.PaintInfo
import pl.dashclever.repairmanagment.estimatecatalogue.VehicleInfo
import java.net.URI
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

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
        estimateDto: EstimateDto
    ): ResponseEntity<EstimateDto> {
        if (estimateRepository.existsByEstimateId(estimateDto.estimateId)) throw ResponseStatusException(HttpStatus.BAD_REQUEST, ALREADY_EXISTS)
        val estimate = estimateDto.toEntity()
        this.estimateRepository.save(estimate)
        return ResponseEntity.created(URI.create("$PATH/${estimate.id}"))
            .body(estimate.toDto())
    }

    data class EstimateFilters(
        val estimateId: String? = null,
        val createdAfter: ZonedDateTime? = null,
        val sortDirection: SortDirection = DESC
    )

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(filters: EstimateFilters?, pageRequestDto: PageRequestDto? = null): Page<EstimateDto> =
        filter(filters ?: EstimateFilters(), pageRequestDto ?: PageRequestDto())

    private fun filter(filters: EstimateFilters, pageRequestDto: PageRequestDto): Page<EstimateDto> {
        var specification: Specification<Estimate>? = null
        if (filters.createdAfter != null) {
            val localDateTimeOfGmt = filters.createdAfter!!.withZoneSameInstant(ZoneId.of("GMT")).toLocalDateTime()
            specification = EstimateSpecifications.createdOnAfter(localDateTimeOfGmt)
        }
        if (filters.estimateId != null) {
            specification = specification?.and(EstimateSpecifications.estimateId(filters.estimateId!!))
                ?: EstimateSpecifications.estimateId(filters.estimateId!!)
        }

        val sort = when (filters.sortDirection) {
            ASC -> Sort.by("createdOn").ascending()
            DESC -> Sort.by("createdOn").descending()
        }
        val pageReq = PageRequest.of(pageRequestDto.pageNumber, pageRequestDto.pageSize, sort)
        val estimatePage = specification?.let { this.estimateRepository.findAll(it, pageReq) } ?: this.estimateRepository.findAll(pageReq)
        return estimatePage.map { it.toDto() }
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    fun delete(@RequestParam estimateId: UUID) {
        return this.estimateRepository.deleteById(estimateId)
    }

    internal data class EstimateDto(
        val id: UUID?,
        @field:Size(min = 1, max = 24, message = "$SIZE_BETWEEN;1;24")
        val estimateId: String,
        @field:Valid
        val vehicleInfo: VehicleInfo,
        @field:Valid
        val paintInfo: PaintInfo,
        val jobs: Set<Job>,
        val creationTimestamp: ZonedDateTime? = null
    )

    private fun EstimateDto.toEntity() =
        Estimate(
            estimateId,
            vehicleInfo,
            paintInfo,
            jobs
        )

    private fun Estimate.toDto(): EstimateDto =
        EstimateDto(
            this.id,
            this.estimateId,
            this.vehicleInfo,
            this.paintInfo,
            this.jobs,
            this.getCreationTimestamp().asGmt()
        )
}
