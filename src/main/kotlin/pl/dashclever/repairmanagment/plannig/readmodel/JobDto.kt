package pl.dashclever.repairmanagment.plannig.readmodel

import java.time.LocalDate

interface JobDto {
    val catalogueJobId: Long
    val manMinutes: Int
    val assignedTo: String?
    val assignedAt: LocalDate?
}
