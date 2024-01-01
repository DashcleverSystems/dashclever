package pl.dashclever.repairmanagment.plannig.model

import java.util.UUID

sealed interface PlanCommand {

    val planId: UUID
}
