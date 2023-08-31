package pl.dashclever.repairmanagment.plannig.readmodel

data class PlanDto(
    val id: String,
    val estimateId: String,
    val technicalRepairTime: Int,
    val jobs: Set<JobDto>
)
