package pl.dashclever.repairmanagment.plannig.readmodel

import java.util.UUID

interface PlanDto {
    val id: UUID
    val estimateId: String
    val technicalRepairTimeInMinutes: Int
}
