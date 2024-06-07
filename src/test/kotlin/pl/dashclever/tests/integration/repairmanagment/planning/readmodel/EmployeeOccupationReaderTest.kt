package pl.dashclever.tests.integration.repairmanagment.planning.readmodel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.dashclever.repairmanagment.plannig.model.PlanFactory
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.plannig.readmodel.EmployeeOccupationDto
import pl.dashclever.repairmanagment.plannig.readmodel.EmployeeOccupationReader
import pl.dashclever.tests.integration.DefaultTestContextConfiguration
import pl.dashclever.tests.integration.spring.TestAccess
import pl.dashclever.tests.integration.spring.TestAccessSetter
import java.time.LocalDate
import java.util.UUID

@SpringBootTest
@DefaultTestContextConfiguration
internal class EmployeeOccupationReaderTest(
    @Autowired private val planRepository: PlanRepository,
    @Autowired private val employeeOccupationReader: EmployeeOccupationReader
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
    fun `should return information about employee occupation at given day`() {
        // given
        val plan = PlanFactory.create(
            estimateId = UUID.randomUUID(),
            jobs = mapOf(
                1L to 60,
                2L to 40
            )
        )
        plan.assign(1L, "employeeId", LocalDate.of(2020, 2, 2))
        plan.assign(2L, "employeeId", LocalDate.of(2020, 2, 2))
        plan.hasRunningRepair = true
        planRepository.save(plan)

        // when
        val result = employeeOccupationReader.findByEmployeeIdWithRunningRepair("employeeId", LocalDate.of(2020, 2, 2))

        // then
        assertThat(result).hasValueSatisfying {
            assertThat(it.employeeId).isEqualTo("employeeId")
            assertThat(it.manMinutes).isEqualTo(100)
        }
    }

    @Test
    fun `should return information about all employees occupation at given day`() {
        // given
        val plan = PlanFactory.create(
            estimateId = UUID.randomUUID(),
            jobs = mapOf(
                1L to 60,
                2L to 40
            )
        )
        plan.assign(1L, "employeeId1", LocalDate.of(2020, 2, 2))
        plan.assign(2L, "employeeId2", LocalDate.of(2020, 2, 2))
        plan.hasRunningRepair = true
        planRepository.save(plan)

        // when
        val result: Set<EmployeeOccupationDto> = employeeOccupationReader.findAllWithRunningRepair(LocalDate.of(2020, 2, 2))

        // then
        assertThat(result).satisfiesExactlyInAnyOrder(
            {
                assertThat(it.employeeId).isEqualTo("employeeId1")
                assertThat(it.manMinutes).isEqualTo(60)
            },
            {
                assertThat(it.employeeId).isEqualTo("employeeId2")
                assertThat(it.manMinutes).isEqualTo(40)
            }
        )
    }

    @Test
    fun `should return sum of all employee occupations of a given plan and all employee occupations of plans having a running repair at given date`() {
        // given
        val today = LocalDate.of(2020, 2, 2)
        val planWithRunningRepair = PlanFactory.create(
            estimateId = UUID.randomUUID(),
            jobs = mapOf(
                1L to 30,
                2L to 70
            )
        )
        planWithRunningRepair.assign(1L, "employeeId1", today)
        planWithRunningRepair.assign(2L, "employeeId2", today)
        planWithRunningRepair.hasRunningRepair = true
        planRepository.save(planWithRunningRepair)

        val plan = PlanFactory.create(
            estimateId = UUID.randomUUID(),
            jobs = mapOf(
                3L to 60,
                4L to 40
            )
        )
        plan.assign(3L, "employeeId1", today)
        plan.assign(4L, "employeeId2", today)
        planRepository.save(plan)

        // when
        val result: Set<EmployeeOccupationDto> = employeeOccupationReader.findAllEmployeeOccupationsForPlanning(
            plan.id,
            today
        )

        // then
        assertThat(result).satisfiesExactlyInAnyOrder(
            {
                assertThat(it.employeeId).isEqualTo("employeeId1")
                assertThat(it.manMinutes).isEqualTo(90)
            },
            {
                assertThat(it.employeeId).isEqualTo("employeeId2")
                assertThat(it.manMinutes).isEqualTo(110)
            }
        )
    }
}
