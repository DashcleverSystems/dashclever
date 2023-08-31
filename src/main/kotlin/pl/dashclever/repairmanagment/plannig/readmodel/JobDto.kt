package pl.dashclever.repairmanagment.plannig.readmodel

import java.time.LocalDate

data class JobDto(
    val catalogueJobId: Long,
    val manMinutes: Int,
    val assignedTo: String? = null,
    val assignedAt: LocalDate? = null
)
