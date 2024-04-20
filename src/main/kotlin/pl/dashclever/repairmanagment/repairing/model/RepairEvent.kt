package pl.dashclever.repairmanagment.repairing.model

import pl.dashclever.commons.events.DomainEvent
import java.util.UUID

sealed interface RepairEvent : DomainEvent {

    val repairId: UUID

    data class StartedRepairOfPlan(
        override val repairId: UUID,
        val planId: UUID
    ) : RepairEvent
}
