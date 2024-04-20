package pl.dashclever.repairmanagment.estimatecatalogue.infrastrucutre

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.estimatecatalogue.RepairEventsHandler
import pl.dashclever.repairmanagment.repairing.model.RepairEvent.StartedRepairOfPlan
import pl.dashclever.spring.TransactionalRunner

@Component("EstimateCatalogueRepairEventsListener")
internal class RepairEventsListener(
    private val repairEventsHandler: RepairEventsHandler,
    private val transactionalRunner: TransactionalRunner
) {

    @EventListener
    fun handle(event: StartedRepairOfPlan) =
        transactionalRunner.run { repairEventsHandler.handle(event) }
}
