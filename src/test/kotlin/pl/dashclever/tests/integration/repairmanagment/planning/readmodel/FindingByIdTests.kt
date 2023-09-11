package pl.dashclever.tests.integration.repairmanagment.planning.readmodel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.dashclever.repairmanagment.plannig.model.PlanFactory
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.plannig.readmodel.JobDto
import pl.dashclever.repairmanagment.plannig.readmodel.PlanDto
import pl.dashclever.repairmanagment.plannig.readmodel.PlanReader
import pl.dashclever.tests.integration.TestcontainersInitializer
import java.time.LocalDate
import java.util.UUID
import java.util.stream.Stream

@SpringBootTest
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class FindingByIdTests @Autowired constructor(
    private val planRepository: PlanRepository,
    private val planReader: PlanReader
) {

    @BeforeEach
    fun setUp() =
        planRepository.deleteAll()

    private companion object {

        @JvmStatic
        fun provideTestData(): Stream<Arguments> {
            return Stream.of(
                `provide fresh plan test data`(),
                `provide modified plan test data`()
            )
        }

        private fun `provide fresh plan test data`(): Arguments {
            val plan = PlanFactory.create(
                estimateId = UUID.randomUUID().toString(),
                jobs = mapOf(
                    1L to 120,
                    2L to 120,
                    3L to 60
                )
            )

            val expectedDto = PlanDto(
                id = plan.id.toString(),
                estimateId = plan.estimateId,
                jobs = setOf(
                    JobDto(1L, 120),
                    JobDto(2L, 120),
                    JobDto(3L, 60)
                ),
                technicalRepairTime = 1
            )
            return Arguments.of(plan, expectedDto)
        }

        private fun `provide modified plan test data`(): Arguments {
            val plan = PlanFactory.create(
                estimateId = UUID.randomUUID().toString(),
                jobs = mapOf(
                    1L to 500,
                    2L to 240
                )
            )
            plan.assign(1, "employeeId", LocalDate.of(2023, 6, 1))

            val expectedDto = PlanDto(
                id = plan.id.toString(),
                estimateId = plan.estimateId,
                jobs = setOf(
                    JobDto(1L, 500, "employeeId", LocalDate.of(2023, 6, 1)),
                    JobDto(2L, 240)
                ),
                technicalRepairTime = 3,
            )
            return Arguments.of(plan, expectedDto)
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    fun `should find plan by id`(plan: pl.dashclever.repairmanagment.plannig.model.Plan, expected: PlanDto) {
        // given
        planRepository.save(plan)

        // when
        val planDto = planReader.findById(plan.id)

        // then
        assertThat(planDto).isEqualTo(expected)
    }
}
