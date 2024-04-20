package pl.dashclever.tests.unit.repairmanagment.planning

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.dashclever.repairmanagment.plannig.model.PlanFactory
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.plannig.model.RepairEventsHandler
import pl.dashclever.repairmanagment.repairing.model.RepairEvent.StartedRepairOfPlan
import java.util.UUID

internal class BlockPlansModificationOnRepairStartTests {

    private val planRepository: PlanRepository = mockk()
    private val repairEventsHandler: RepairEventsHandler = RepairEventsHandler(planRepository)

    @Test
    fun `should block plans modifications of an estimate which repair started`() {
        // given
        val repairId = UUID.randomUUID()
        val estimateId = UUID.randomUUID()
        val planOfRepair = PlanFactory.create(
            estimateId,
            jobs = mapOf(
                1L to 60,
                2L to 40
            )
        )
        val plan2 = PlanFactory.create(
            estimateId,
            jobs = mapOf(
                1L to 60,
                2L to 40
            )
        )
        every { planRepository.findByIdOrThrow(planOfRepair.id) } returns planOfRepair
        val plansOfEstimate = setOf(planOfRepair, plan2)
        every { planRepository.findAllByEstimateId(estimateId) } returns plansOfEstimate

            // when
        repairEventsHandler.handle(StartedRepairOfPlan(repairId, planOfRepair.id))

        // then
        assertThat(plansOfEstimate).allMatch { it.canBeModified.not() }
    }
}
