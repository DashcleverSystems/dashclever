package pl.dashclever.repairmanagment.estimatecatalogue.infrastrucutre

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.dashclever.repairmanagment.estimatecatalogue.RepairEventsHandler
import pl.dashclever.repairmanagment.repairing.model.RepairEvent.StartedRepairOfPlan
import pl.dashclever.spring.TransactionalRunner
import pl.dashclever.spring.events.cloudstreams.CloudStreamConsumer

@Configuration("EstimateCatalogueRepairEventsListener")
internal class RepairEventsListener(
    private val repairEventsHandler: RepairEventsHandler,
    private val transactionalRunner: TransactionalRunner
) {

    @Bean
    fun indicateOngoingRepairOfEstimate() = CloudStreamConsumer<StartedRepairOfPlan> { event ->
        transactionalRunner.run { repairEventsHandler.handle(event) }
    }
}
