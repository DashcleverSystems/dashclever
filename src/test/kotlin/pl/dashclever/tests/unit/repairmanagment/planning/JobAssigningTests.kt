package pl.dashclever.tests.unit.repairmanagment.planning

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import pl.dashclever.publishedlanguage.DomainException
import pl.dashclever.repairmanagment.plannig.model.Plan
import pl.dashclever.repairmanagment.plannig.model.PlanEvent.TaskAssigned
import pl.dashclever.repairmanagment.plannig.model.PlanFactory
import java.time.LocalDate
import java.util.stream.Stream

internal class JobAssigningTests {

    private companion object {

        @JvmStatic
        fun providePassingPlans(): Stream<Arguments> {

            fun jobs(number: Long): Map<Long, Int> {
                val jobs = mutableMapOf<Long, Int>()
                for (i in 1L..number) {
                    jobs[i] = 120
                }
                return jobs
            }

            fun preparePlan(numberOfJobs: Long, assignAt: List<LocalDate>): Plan {
                val plan = PlanFactory.create("testEstimateId", jobs(numberOfJobs))
                assignAt.forEachIndexed { i, day ->
                    plan.assign(i + 1L, "employeeId", day)
                }
                return plan
            }

            return Stream.of(
                Arguments.of(PlanFactory.create("simplePlanEstimateId", mapOf(1L to 120))),
                Arguments.of(
                    preparePlan(
                        2L,
                        listOf(
                            LocalDate.of(2023, 1, 6)
                        )
                    )
                ),
                Arguments.of(
                    preparePlan(
                        8L,
                        listOf(
                            LocalDate.of(2023, 1, 5),
                            LocalDate.of(2023, 1, 4)
                        )
                    )
                )
            )
        }
    }

    @ParameterizedTest
    @MethodSource("providePassingPlans")
    fun `GIVEN plan with jobs SHOULD assign job`(passingPlan: pl.dashclever.repairmanagment.plannig.model.Plan) {
        // given
        val expected = TaskAssigned(passingPlan.id.toString(), "1", "employeeId")

        // when
        val result = passingPlan.assign(1, "employeeId", LocalDate.of(2023, 1, 6))

        // then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `GIVEN plan with jobs SHOULD not allow to assign job after maximum repair technical time`() {
        // given
        val plan = PlanFactory.create(
            estimateId = "estimateId",
            jobs = mapOf(
                1L to 120,
                2L to 120,
                3L to 120
            )
        )
        plan.assign(1, "employeeId", LocalDate.of(2023, 1, 6))

        // when & then
        assertThrows<DomainException> {
            plan.assign(2, "employeeId", LocalDate.of(2023, 1, 8))
        }
    }

    @Test
    fun `GIVEN plan with jobs SHOULD allow to assign job with time`() {
        // given
        val plan = PlanFactory.create(
            estimateId = "estimateId",
            jobs = mapOf(
                1L to 120,
                2L to 120,
                3L to 120
            )
        )
        plan.assign(1, "employeeId", LocalDate.of(2023, 1, 6))

        // when
        val result = plan.assignWithTime(2, "employeeId", LocalDate.of(2023, 1, 7), 8)

        // then
        assertThat(result).isEqualTo(
            TaskAssigned(
                planId = plan.id.toString(),
                jobId = "2",
                employeeId = "employeeId"
            )
        )
    }

    @Test
    fun `GIVEN plan with jobs SHOULD not allow to assign job with time within not working hours`() {
        // given
        val plan = PlanFactory.create(
            estimateId = "estimateId",
            jobs = mapOf(
                1L to 120,
                2L to 120,
                3L to 120
            )
        )
        plan.assign(1, "employeeId", LocalDate.of(2023, 1, 6))

        // when
        assertThrows<DomainException> {
            plan.assignWithTime(2, "employeeId", LocalDate.of(2023, 1, 7), 24)
        }
    }
}
