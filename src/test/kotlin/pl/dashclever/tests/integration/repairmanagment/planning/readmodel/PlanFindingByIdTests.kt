package pl.dashclever.tests.integration.repairmanagment.planning.readmodel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.dashclever.repairmanagment.plannig.model.Plan
import pl.dashclever.repairmanagment.plannig.model.PlanFactory
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.plannig.readmodel.PlanDto
import pl.dashclever.repairmanagment.plannig.readmodel.PlanReader
import pl.dashclever.tests.integration.TestcontainersInitializer
import java.time.LocalDate
import java.util.UUID
import java.util.stream.Stream

@SpringBootTest
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class PlanFindingByIdTests @Autowired constructor(
    private val planRepository: PlanRepository,
    private val planReader: PlanReader
) {

    @AfterEach
    fun tearDown() =
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

            val assertions = { planDto: PlanDto ->
                assertThat(planDto.id).isEqualTo(plan.id)
                assertThat(planDto.estimateId).isEqualTo(plan.estimateId)
                assertThat(planDto.technicalRepairTimeInMinutes).isEqualTo(300)
                assertThat(planDto.createdOn).isNotNull()
            }
            return Arguments.of(plan, assertions)
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

            val assertions = { planDto: PlanDto ->
                assertThat(planDto.id).isEqualTo(plan.id)
                assertThat(planDto.estimateId).isEqualTo(plan.estimateId)
                assertThat(planDto.technicalRepairTimeInMinutes).isEqualTo(740)
                assertThat(planDto.createdOn).isNotNull()
            }
            return Arguments.of(plan, assertions)
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    fun `should find plan by id`(plan: Plan, assertions: (planDto: PlanDto) -> Unit) {
        // given
        planRepository.save(plan)

        // when
        val planDto: PlanDto = planReader.findById(plan.id).get()

        // then
        assertThat(planDto).satisfies(assertions)
    }
}
