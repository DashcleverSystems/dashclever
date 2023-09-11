package pl.dashclever.repairmanagment.plannig.readmodel

import java.time.LocalDate
import java.util.UUID

interface PlanReader {
    fun findById(id: UUID): PlanDto
    fun findByEstimateId(estimateId: String): Set<PlanDto>
    fun findByDateRange(from: LocalDate, to: LocalDate): Set<PlanDto>
}
