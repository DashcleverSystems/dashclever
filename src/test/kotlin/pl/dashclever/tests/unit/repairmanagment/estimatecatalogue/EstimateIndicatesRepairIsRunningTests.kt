package pl.dashclever.tests.unit.repairmanagment.estimatecatalogue

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository
import pl.dashclever.repairmanagment.estimatecatalogue.RepairEventsHandler
import pl.dashclever.repairmanagment.plannig.model.PlanFactory
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.repairing.model.RepairEvent.StartedRepairOfPlan
import pl.dashclever.tests.integration.repairmanagment.estimatecatalogue.EstimateBuilder
import java.util.UUID

internal class EstimateIndicatesRepairIsRunningTests {

    private val planRepository: PlanRepository = mockk()
    private val estimateRepository: EstimateRepository = mockk()
    private val repairEventsHandler: RepairEventsHandler = RepairEventsHandler(planRepository, estimateRepository)

    @Test
    fun `should mark estimate when a repair starts`() {
        // given
        val estimateOfRepair = EstimateBuilder { }
        val planOfRepair = PlanFactory.create(
            estimateOfRepair.id,
            jobs = mapOf(1L to 60, 2L to 120)
        )
        every { planRepository.findByIdOrThrow(planOfRepair.id) } returns planOfRepair
        every { estimateRepository.findById(planOfRepair.estimateId) } returns estimateOfRepair

        // when
        repairEventsHandler.handle(StartedRepairOfPlan(repairId = UUID.randomUUID(), planOfRepair.id))

        // then
        assertThat(estimateOfRepair.hasRepairInProgress).isTrue()
    }
}
