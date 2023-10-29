package pl.dashclever.tests.integration.repairmanagment.planning.readmodel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.repairmanagment.plannig.model.PlanFactory
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.plannig.readmodel.JobDto
import pl.dashclever.repairmanagment.plannig.readmodel.JobReader
import pl.dashclever.tests.integration.TestcontainersInitializer
import pl.dashclever.tests.integration.spring.TestAccess
import pl.dashclever.tests.integration.spring.TestAccessSetter
import java.time.LocalDate
import java.util.UUID

@SpringBootTest
@Transactional
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class JobReaderTest(
    @Autowired private val planRepository: PlanRepository,
    @Autowired private val jobReader: JobReader
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
    fun `should return not assigned jobs of a plan`() {
        // given
        val plan = PlanFactory.create(
            estimateId = UUID.randomUUID().toString(),
            jobs = mapOf(
                1L to 60,
                2L to 60
            )
        )
        planRepository.save(plan)

        // when
        val result: Set<JobDto> = jobReader.findByPlanId(testAccess.workshopId, plan.id)

        // then
        assertThat(result).hasSize(2)
        assertThat(result).anySatisfy {
            assertThat(it.catalogueJobId).isEqualTo(1L)
            assertThat(it.manMinutes).isEqualTo(60L)
        }
        assertThat(result).anySatisfy {
            assertThat(it.catalogueJobId).isEqualTo(2L)
            assertThat(it.manMinutes).isEqualTo(60L)
        }
    }

    @Test
    fun `should return assigned job of a plan`() {
        // given
        val plan = PlanFactory.create(
            estimateId = UUID.randomUUID().toString(),
            jobs = mapOf(
                1L to 60
            )
        )
        plan.assign(1L, "employeeId", LocalDate.of(2020, 2, 2))
        planRepository.save(plan)

        // when
        val result: Set<JobDto> = jobReader.findByPlanId(testAccess.workshopId, plan.id)

        // then
        assertThat(result).singleElement().satisfies(
            {
                assertThat(it.catalogueJobId).isEqualTo(1L)
                assertThat(it.manMinutes).isEqualTo(60L)
                assertThat(it.assignedTo).isEqualTo("employeeId")
                assertThat(it.assignedAt).isEqualTo(LocalDate.of(2020, 2, 2))
            }
        )
    }
}
