package pl.dashclever.repairmanagment.plannig.infrastructure

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.dashclever.repairmanagment.plannig.model.RepairEventsHandler
import pl.dashclever.repairmanagment.repairing.model.RepairEvent.StartedRepairOfPlan
import pl.dashclever.spring.TransactionalRunner
import pl.dashclever.spring.events.cloudstreams.CloudStreamConsumer

@Configuration("PlanningRepairEventsListener")
internal class RepairEventsListener(
    private val repairEventsHandler: RepairEventsHandler,
    private val transactionalRunner: TransactionalRunner
) {

    @Bean
    fun blockPlansModifications() = CloudStreamConsumer<StartedRepairOfPlan> { event ->
        transactionalRunner.run { repairEventsHandler.handle(event) }
    }
}
