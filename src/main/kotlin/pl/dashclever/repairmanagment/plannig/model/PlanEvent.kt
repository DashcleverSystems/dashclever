package pl.dashclever.repairmanagment.plannig.model

sealed interface PlanEvent {

    val planId: String

    data class TaskAssigned(
        override val planId: String,
        val jobId: String,
        val employeeId: String,
    ) : PlanEvent
}
