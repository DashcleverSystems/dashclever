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
import pl.dashclever.repairmanagment.estimatecatalogue.JobType
import pl.dashclever.repairmanagment.plannig.model.PlanFactory
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.plannig.readmodel.JobDto
import pl.dashclever.repairmanagment.plannig.readmodel.JobReader
import pl.dashclever.tests.integration.TestcontainersInitializer
import pl.dashclever.tests.integration.repairmanagment.estimatecatalogue.EstimateBuilder
import pl.dashclever.tests.integration.repairmanagment.estimatecatalogue.JobBuilder
import pl.dashclever.tests.integration.spring.TestAccess
import pl.dashclever.tests.integration.spring.TestAccessSetter
import java.time.LocalDate
import java.util.UUID

@SpringBootTest
@Transactional
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class JobReaderTest(
    @Autowired private val planRepository: PlanRepository,
    @Autowired private val estimateRepository: EstimateRepository,
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
    fun `should return all jobs of a plan`() {
        // given
        val jobs = setOf(
            JobBuilder {
                this.manMinutes = 60
                this.description = "job1"
                this.jobType = JobType.LABOUR
            },
            JobBuilder {
                this.manMinutes = 120
                this.description = "job2"
                this.jobType = JobType.VARNISHING
            }
        )
        val estimate = EstimateBuilder { this.jobs = jobs }
        estimateRepository.save(estimate)

        val plan = PlanFactory.create(
            estimateId = UUID.randomUUID(),
            jobs = jobs.associate { it.id!! to it.manMinutes }
        )
        planRepository.save(plan)

        // when
        val result: Set<JobDto> = jobReader.findByPlanId(testAccess.workshopId, plan.id)

        // then
        assertThat(result).satisfiesExactlyInAnyOrder(
            {
                assertThat(it.manMinutes).isEqualTo(60L)
                assertThat(it.jobDescription).isEqualTo("job1")
                assertThat(it.jobType).isEqualTo(JobType.LABOUR.name)
            },
            {
                assertThat(it.manMinutes).isEqualTo(120L)
                assertThat(it.jobDescription).isEqualTo("job2")
                assertThat(it.jobType).isEqualTo(JobType.VARNISHING.name)
            }
        )
    }

    @Test
    fun `should return assigned job of a plan`() {
        // given
        val job = JobBuilder { this.manMinutes = 60 }
        val estimate = EstimateBuilder { this.jobs = setOf(job) }
        estimateRepository.save(estimate)
        val plan = PlanFactory.create(
            estimateId = UUID.randomUUID(),
            jobs = estimate.jobs.associate { it.id!! to it.manMinutes }
        )
        plan.assign(job.id!!, "employeeId", LocalDate.of(2020, 2, 2))
        planRepository.save(plan)

        // when
        val result: Set<JobDto> = jobReader.findByPlanId(testAccess.workshopId, plan.id)

        // then
        assertThat(result).singleElement().satisfies(
            {
                assertThat(it.catalogueJobId).isEqualTo(job.id!!)
                assertThat(it.manMinutes).isEqualTo(60L)
                assertThat(it.assignedTo).isEqualTo("employeeId")
                assertThat(it.assignedAt).isEqualTo(LocalDate.of(2020, 2, 2))
            }
        )
    }
}
