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
import java.util.*

@SpringBootTest
@Transactional
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class PlanFindingByIdTests @Autowired constructor(
    private val estimateRepository: EstimateRepository,
    private val planRepository: PlanRepository,
    private val planReader: PlanReader
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
    fun `should find plan by id`() {
        // given
        val jobs = setOf(
            JobBuilder { this.manMinutes = 120 },
            JobBuilder { this.manMinutes = 120 },
            JobBuilder { this.manMinutes = 60 }
        )
        val estimate = EstimateBuilder {
            this.estimateId = "24/2022WK"
            this.jobs = jobs
        }
        estimateRepository.save(estimate)

        val plan = PlanFactory.create(
            estimateId = estimate.id,
            jobs = jobs.associate { it.id!! to it.manMinutes }
        )
        planRepository.save(plan)

        // when
        val result: Optional<PlanDto> = planReader.findById(testAccess.workshopId, plan.id)

        // then
        assertThat(result).hasValueSatisfying { planDto ->
            assertThat(planDto.id).isEqualTo(plan.id)
            assertThat(planDto.estimateId).isEqualTo(plan.estimateId.toString())
            assertThat(planDto.estimateName).isEqualTo("24/2022WK")
            assertThat(planDto.technicalRepairTimeInMinutes).isEqualTo(300)
            assertThat(planDto.createdOn).isNotNull()
        }
    }

    @Test
    fun `should find plans by estimate id`() {
        // given
        val jobs = setOf(
            JobBuilder { this.manMinutes = 120 },
            JobBuilder { this.manMinutes = 120 },
            JobBuilder { this.manMinutes = 60 }
        )
        val estimate = EstimateBuilder {
            this.estimateId = "25/2022WK"
            this.jobs = jobs
        }
        estimateRepository.save(estimate)

        val plan = PlanFactory.create(
            estimateId = estimate.id,
            jobs = jobs.associate { it.id!! to it.manMinutes }
        )
        planRepository.save(plan)

        // when
        val result: Set<PlanDto> = planReader.findByEstimateId(testAccess.workshopId, estimate.id)

        // then
        assertThat(result).singleElement().satisfies(
            { planDto ->
                assertThat(planDto.id).isEqualTo(plan.id)
                assertThat(planDto.estimateId).isEqualTo(plan.estimateId.toString())
                assertThat(planDto.estimateName).isEqualTo("25/2022WK")
                assertThat(planDto.technicalRepairTimeInMinutes).isEqualTo(300)
                assertThat(planDto.createdOn).isNotNull()
            }
        )
    }
}
