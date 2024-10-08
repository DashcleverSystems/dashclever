package pl.dashclever.tests.integration.repairmanagment.planning.readmodel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.data.auditing.AuditingHandler
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.commons.time.LocalDateTimeHelper.asGmt
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository
import pl.dashclever.repairmanagment.plannig.model.PlanFactory
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.plannig.readmodel.PlanReader
import pl.dashclever.repairmanagment.plannig.readmodel.PlanReader.PlanFilters
import pl.dashclever.tests.integration.DefaultTestContextConfiguration
import pl.dashclever.tests.integration.repairmanagment.estimatecatalogue.EstimateBuilder
import pl.dashclever.tests.integration.spring.TestAccess
import pl.dashclever.tests.integration.spring.TestAccessSetter
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension::class)
@DefaultTestContextConfiguration
internal class PlanFindingMixFiltersTests(
    @Autowired private val planRepository: PlanRepository,
    @Autowired private val planReader: PlanReader,
    @Autowired private val estimateRepository: EstimateRepository,
    @Autowired @SpyBean
    private val auditingHandler: AuditingHandler
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
    fun `should return plans matching given filters`() {
        // given
        auditingHandler.setDateTimeProvider { Optional.of(LocalDateTime.of(2023, 1, 1, 12, 0)) }
        val estimate1 = EstimateBuilder { estimateName = "01/2022WK" }
        estimateRepository.save(estimate1)
        val plan1 = PlanFactory.create(
            estimateId = estimate1.id,
            jobs = estimate1.jobs.associate { it.id!! to it.manMinutes }
        )
        planRepository.save(plan1)

        auditingHandler.setDateTimeProvider { Optional.of(LocalDateTime.of(2023, 1, 1, 12, 1)) }
        val estimate2 = EstimateBuilder { estimateName = "02/2022WK" }
        estimateRepository.save(estimate2)
        val plan2 = PlanFactory.create(
            estimateId = estimate2.id,
            jobs = estimate2.jobs.associate { it.id!! to it.manMinutes }
        )
        planRepository.save(plan2)

        val filters = PlanFilters(createdAfter = LocalDateTime.of(2023, 1, 1, 11, 59), estimateName = "02/2022WK")

        // when
        val result = planReader.filter(filters, PageRequest.of(0, 10))

        // then
        assertThat(result.content).singleElement().satisfies(
            {
                assertThat(it.createdOn).isAfterOrEqualTo(LocalDateTime.of(2023, 1, 1, 11, 59).asGmt())
                assertThat(it.estimateName).isEqualTo("02/2022WK")
            }
        )
    }
}
