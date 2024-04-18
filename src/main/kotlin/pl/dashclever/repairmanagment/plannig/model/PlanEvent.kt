package pl.dashclever.repairmanagment.plannig.model

sealed interface PlanEvent {

    val planId: String

    data class JobAssigned(
        override val planId: String,
        val jobId: String,
        val employeeId: String
    ) : PlanEvent

    data class JobUnassigned(
        override val planId: String,
        val jobId: String
    ) : PlanEvent
}
