package pl.dashclever.tests.integration.repairmanagment.planning.readmodel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository
import pl.dashclever.repairmanagment.plannig.model.PlanFactory
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.plannig.readmodel.PlanDto
import pl.dashclever.repairmanagment.plannig.readmodel.PlanReader
import pl.dashclever.tests.integration.TestcontainersInitializer
import pl.dashclever.tests.integration.repairmanagment.estimatecatalogue.EstimateBuilder
import pl.dashclever.tests.integration.repairmanagment.estimatecatalogue.JobBuilder
import pl.dashclever.tests.integration.spring.TestAccess
import pl.dashclever.tests.integration.spring.TestAccessSetter
import java.time.LocalDate
import java.util.*

@SpringBootTest
@Transactional
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class PlanFindingWithinDatesTest(
    @Autowired private val planRepository: PlanRepository,
    @Autowired private val planReader: PlanReader,
    @Autowired private val estimateRepository: EstimateRepository
) {

    private val testAccessSetter = TestAccessSetter()
    private val testAccess = TestAccess(
        accountId = UUID.randomUUID(),
        authorities = emptySet(),
        workshopId = UUID.randomUUID()
    )

    @BeforeEach
    fun setUp() {
        testAccessSetter.setAccess(testAccess)
    }

    @AfterEach
    fun tearDown() {
        testAccessSetter.setAccess(null)
    }

    @Test
    fun `should not find any`() {
        // given
        val estimate = EstimateBuilder {
            this.jobs = setOf(
                JobBuilder { this.manMinutes = 120 },
                JobBuilder { this.manMinutes = 240 }
            )
        }
        estimateRepository.save(estimate)
        val plan = PlanFactory.create(
            estimateId = UUID.randomUUID(),
            jobs = estimate.jobs.associate { it.id!! to it.manMinutes }
        )
        plan.assign(estimate.jobs.first().id!!, "employeeId", LocalDate.of(2023, 1, 6))
        planRepository.save(plan)

        // when
        val result: Set<PlanDto> = planReader.findByDateRange(
            testAccess.workshopId,
            from = LocalDate.of(2023, 1, 7),
            to = LocalDate.of(2023, 1, 8)
        )

        // then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should find two within given dates range`() {
        // given
        val estimate = EstimateBuilder {
            this.estimateId = "01/2022WK"
            this.jobs = setOf(
                JobBuilder { this.manMinutes = 120 },
                JobBuilder { this.manMinutes = 240 }
            )
        }
        estimateRepository.save(estimate)
        val plan1 = PlanFactory.create(
            estimateId = estimate.id,
            jobs = estimate.jobs.associate { it.id!! to it.manMinutes }
        )
        plan1.assign(estimate.jobs.first().id!!, "employeeId", LocalDate.of(2023, 1, 6))
        planRepository.save(plan1)
        val plan2 = PlanFactory.create(
            estimateId = estimate.id,
            jobs = estimate.jobs.associate { it.id!! to it.manMinutes }
        )
        plan2.assign(estimate.jobs.first().id!!, "employeeId", LocalDate.of(2023, 1, 8))
        planRepository.save(plan2)

        // when
        val result: Set<PlanDto> = planReader.findByDateRange(
            testAccess.workshopId,
            LocalDate.of(1999, 1, 1),
            LocalDate.of(2024, 1, 1)
        )

        // then
        assertThat(result).satisfiesExactlyInAnyOrder(
            {
                assertThat(it.estimateName).isEqualTo("01/2022WK")
                assertThat(it.estimateId).isEqualTo(estimate.id.toString())
                assertThat(it.technicalRepairTimeInMinutes).isEqualTo(360)
            },
            {
                assertThat(it.estimateName).isEqualTo("01/2022WK")
                assertThat(it.estimateId).isEqualTo(estimate.id.toString())
                assertThat(it.technicalRepairTimeInMinutes).isEqualTo(360)
            }
        )
    }
}
