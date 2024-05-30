package pl.dashclever.tests.integration.repairmanagment.planning.infrastrucutre.repostiory

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.commons.security.WithAuthorities.Authority
import pl.dashclever.repairmanagment.plannig.infrastructure.repository.PlanWorkshopSecuredRepository
import pl.dashclever.repairmanagment.plannig.infrastructure.repository.WorkshopPlanSecurityRecordRepository
import pl.dashclever.repairmanagment.plannig.model.PlanFactory
import pl.dashclever.tests.integration.DefaultTestContextConfiguration
import pl.dashclever.tests.integration.spring.TestAccess
import pl.dashclever.tests.integration.spring.TestAccessSetter
import java.util.UUID

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@DefaultTestContextConfiguration
internal class PlanWorkshopSecuredRepositoryTests(
    @Autowired private val planWorkshopSecuredRepository: PlanWorkshopSecuredRepository,
    @Autowired private val workshopPlanSecurityRecordRepository: WorkshopPlanSecurityRecordRepository
) {

    private val testAccessSetter = TestAccessSetter()

    @Test
    fun `should create security record`() {
        // given
        val access = TestAccess(
            accountId = UUID.randomUUID(),
            authorities = Authority.values().toSet(),
            workshopId = UUID.randomUUID()
        )
        testAccessSetter.setAccess(access)
        val plan = PlanFactory.create(
            estimateId = UUID.randomUUID(),
            jobs = mapOf(2L to 60)
        )

        // when
        planWorkshopSecuredRepository.save(plan)

        // then
        val didCreateSecurityRecord = workshopPlanSecurityRecordRepository.existsByPlanId(plan.id)
        assertThat(didCreateSecurityRecord).isTrue()
    }

    @Test
    fun `should find plan by id`() {
    }

    @Test
    fun `should not find plan if it does not belong to current access workshop`() {
        // given
        val access = TestAccess(
            accountId = UUID.randomUUID(),
            authorities = Authority.values().toSet(),
            workshopId = UUID.randomUUID()
        )
        testAccessSetter.setAccess(access)
        val plan = PlanFactory.create(
            estimateId = UUID.randomUUID(),
            jobs = mapOf(2L to 60)
        )
        planWorkshopSecuredRepository.save(plan)
        testAccessSetter.setAccess(access.copy(workshopId = UUID.randomUUID()))

        // when
        val result = planWorkshopSecuredRepository.findById(plan.id)

        // then
        assertThat(result).isNull()
    }

    @Test
    fun `should find plans of given estimate and workshop`() {
        // given
        val access = TestAccess(
            accountId = UUID.randomUUID(),
            authorities = Authority.values().toSet(),
            workshopId = UUID.randomUUID()
        )
        testAccessSetter.setAccess(access)
        fun savePlan(estimateId: UUID) = PlanFactory.create(estimateId, jobs = mapOf(2L to 60)).run {
            planWorkshopSecuredRepository.save(this)
        }
        val estimateId = UUID.randomUUID()
        savePlan(estimateId)
        savePlan(estimateId)
        savePlan(UUID.randomUUID())

        // when
        val result = planWorkshopSecuredRepository.findAllByEstimateId(estimateId)

        // then
        assertThat(result).hasSize(2)
        assertThat(result).allMatch { it.estimateId == estimateId }
    }
}
