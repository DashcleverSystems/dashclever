package pl.dashclever.repairmanagment.plannig.infrastructure.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.repairmanagment.plannig.readmodel.PlanDto
import pl.dashclever.repairmanagment.plannig.readmodel.PlanReader
import java.time.LocalDate
import java.util.UUID

private const val PATH = "/api/planning"

@RestController
@RequestMapping(PATH)
@Tag(name = "planning-api")
internal class PlanReadRestApi(
    private val planReader: PlanReader,
    private val currentAccessProvider: CurrentAccessProvider
) {

    @GetMapping("/{planId}")
    fun findById(@PathVariable planId: UUID): PlanDto =
        planReader.findById(this.currentAccessProvider.currentWorkshop().workshopId, planId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

    @GetMapping
    fun filter(
        @RequestParam(name = "estimateId", required = false) estimateId: UUID?,
        @RequestParam(name = "from", required = false) from: LocalDate?,
        @RequestParam(name = "to", required = false) to: LocalDate?
    ): List<PlanDto> {
        if (from != null || to != null) {
            return findByDateRange(from, to).toList()
        }
        val currentAccess = this.currentAccessProvider.currentWorkshop()
        return planReader.findByEstimateId(
            currentAccess.workshopId,
            estimateId ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "estimate id has to specified")
        ).toList()
    }

    private fun findByDateRange(from: LocalDate?, to: LocalDate?): Set<PlanDto> {
        if (from == null || to == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "from and to date has to specified")
        }
        val currentAccess = this.currentAccessProvider.currentWorkshop()
        return planReader.findByDateRange(currentAccess.workshopId, from, to)
    }
}
