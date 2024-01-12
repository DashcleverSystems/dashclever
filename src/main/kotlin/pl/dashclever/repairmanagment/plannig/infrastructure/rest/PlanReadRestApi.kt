package pl.dashclever.repairmanagment.plannig.infrastructure.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.dashclever.commons.paging.PageRequestDto
import pl.dashclever.commons.paging.PagingInfo
import pl.dashclever.repairmanagment.plannig.readmodel.PlanReader
import pl.dashclever.repairmanagment.plannig.readmodel.PlanReader.PlanDto
import pl.dashclever.repairmanagment.plannig.readmodel.PlanReader.PlanFilters
import java.util.UUID

private const val PATH = "/api/planning"

@RestController()
@RequestMapping(PATH, produces = [MediaType.APPLICATION_JSON_VALUE])
@Tag(name = "planning-api")
internal class PlanReadRestApi(
    private val planReader: PlanReader
) {

    @GetMapping("/{planId}")
    fun findById(@PathVariable planId: UUID): PlanDto =
        planReader.findById(planId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

    @GetMapping
    fun filter(
        filters: PlanFilters? = null,
        pageRequestDto: PageRequestDto? = null
    ): PagingInfo<PlanDto> {
        val pageRequest = PageRequest.of(
            pageRequestDto?.pageNumber ?: 0,
            pageRequestDto?.pageSize ?: 0
        )
        return this.planReader.filter(filters ?: PlanFilters(), pageRequest)
    }
}
