package pl.dashclever.tests.integration.repairmanagment.planning.readmodel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository
import pl.dashclever.repairmanagment.plannig.model.PlanFactory
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.plannig.readmodel.PlanReader
import pl.dashclever.repairmanagment.plannig.readmodel.PlanReader.PlanFilters
import pl.dashclever.tests.integration.DefaultTestContextConfiguration
import pl.dashclever.tests.integration.repairmanagment.estimatecatalogue.EstimateBuilder
import pl.dashclever.tests.integration.spring.TestAccess
import pl.dashclever.tests.integration.spring.TestAccessSetter
import java.util.UUID

@SpringBootTest
@Transactional
@DefaultTestContextConfiguration
internal class PlanFilteringWithRunningRepair(
    @Autowired private val estimateRepository: EstimateRepository,
    @Autowired private val planRepository: PlanRepository,
    @Autowired private val planReader: PlanReader
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
    fun `should find only plans without running repair`() {
        // given
        val estimate1 = EstimateBuilder { estimateName = "01/2022WK" }
        estimateRepository.save(estimate1)
        val plan1 = PlanFactory.create(
            estimateId = estimate1.id,
            jobs = estimate1.jobs.associate { it.id!! to it.manMinutes }
        ).apply { hasRunningRepair = false }
        planRepository.save(plan1)

        val estimate2 = EstimateBuilder { estimateName = "02/2022WK" }
        estimateRepository.save(estimate2)
        val plan2 = PlanFactory.create(
            estimateId = estimate2.id,
            jobs = estimate2.jobs.associate { it.id!! to it.manMinutes }
        ).apply { hasRunningRepair = true }
        planRepository.save(plan2)

        val filters = PlanFilters(hasRunningRepair = false)

        // when
        val result = planReader.filter(filters, PageRequest.of(0, 10))

        // then
        assertThat(result.totalElements).isEqualTo(1)
        assertThat(result.content).singleElement().satisfies(
            {
                assertThat(it.id).isEqualTo(plan1.id)
            }
        )
    }
}
