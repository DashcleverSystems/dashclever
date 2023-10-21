package pl.dashclever.repairmanagment.plannig.infrastructure.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.dashclever.repairmanagment.plannig.readmodel.PlanDto
import pl.dashclever.repairmanagment.plannig.readmodel.PlanReader
import java.time.LocalDate
import java.util.UUID

private const val PATH = "/api/planning"

@RestController
@RequestMapping(PATH)
internal class PlanReadController(
    private val planReader: PlanReader,
) {

    @GetMapping("/{planId}")
    fun findById(@PathVariable planId: UUID): PlanDto =
        planReader.findById(planId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

    @GetMapping
    fun filter(
        @RequestParam(name = "estimateId", required = false) estimateId: UUID?,
        @RequestParam(name = "from", required = false) from: LocalDate?,
        @RequestParam(name = "to", required = false) to: LocalDate?,
    ): Set<PlanDto> {
        if (from != null || to != null) {
            return findByDateRange(from, to)
        }
        return planReader.findByEstimateId(
            estimateId ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "estimate id has to specified")
        )
    }

    private fun findByDateRange(from: LocalDate?, to: LocalDate?): Set<PlanDto> {
        if (from == null || to == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "from and to date has to specified")
        }
        return planReader.findByDateRange(from, to)
    }
}
