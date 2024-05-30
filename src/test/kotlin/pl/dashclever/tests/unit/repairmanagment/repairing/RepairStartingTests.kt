package pl.dashclever.tests.unit.repairmanagment.repairing

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import pl.dashclever.commons.events.DomainEvents
import pl.dashclever.commons.exception.DomainException
import pl.dashclever.repairmanagment.plannig.model.Plan
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.repairing.application.RepairRepository
import pl.dashclever.repairmanagment.repairing.application.RepairService
import pl.dashclever.repairmanagment.repairing.model.RepairEvent.StartedRepairOfPlan
import java.util.UUID

internal class RepairStartingTests {

    private val repairRepository: RepairRepository = mockk(relaxed = true)
    private val planRepository: PlanRepository = mockk(relaxUnitFun = true)
    private val domainEvents: DomainEvents = mockk(relaxUnitFun = true)
    private val repairService: RepairService = RepairService(planRepository, repairRepository, domainEvents)

    @Test
    fun `should create a repair`() {
        // given
        val plan: Plan = mockk()
        val planId = UUID.randomUUID()
        every { plan.id } returns planId
        val estimateId = UUID.randomUUID()
        every { plan.estimateId } returns estimateId
        every { planRepository.findByIdOrThrow(planId) } returns plan
        every { planRepository.findAllByEstimateId(estimateId) } returns setOf(plan)
        every { repairRepository.anyRunningRepairOfPlanIdIn(setOf(planId)) } returns false

        // when
        val repair = repairService.createRepairOfPlan(planId)

        // then
        assertThat(repair).satisfies(
            { assertThat(it.planId).isEqualTo(planId) }
        )
    }

    @Test
    fun `should publish event informing of which plan repair has started`() {
        // given
        val plan: Plan = mockk()
        val planId = UUID.randomUUID()
        every { plan.id } returns planId
        val estimateId = UUID.randomUUID()
        every { plan.estimateId } returns estimateId
        every { planRepository.findByIdOrThrow(planId) } returns plan
        every { planRepository.findAllByEstimateId(estimateId) } returns setOf(plan)
        every { repairRepository.anyRunningRepairOfPlanIdIn(setOf(planId)) } returns false

        // when
        val repair = repairService.createRepairOfPlan(planId)

        // then
        verify { domainEvents.publish(StartedRepairOfPlan(repair.id, planId)) }
    }

    @Test
    fun `should not allow to start a repair if there is another repair started of the plan of the same estimate`() {
        // given
        val plan: Plan = mockk()
        val planId = UUID.randomUUID()
        every { plan.id } returns planId
        val estimateId = UUID.randomUUID()
        every { plan.estimateId } returns estimateId
        every { planRepository.findByIdOrThrow(planId) } returns plan
        every { planRepository.findAllByEstimateId(estimateId) } returns setOf(plan)
        every { repairRepository.anyRunningRepairOfPlanIdIn(setOf(planId)) } returns true

        // when & then
        assertThatThrownBy { repairService.createRepairOfPlan(planId) }
            .isInstanceOf(DomainException::class.java)
            .hasMessageContaining("Tried to start another repair of estimate id: ${plan.estimateId} with planId: $planId")
    }
}
