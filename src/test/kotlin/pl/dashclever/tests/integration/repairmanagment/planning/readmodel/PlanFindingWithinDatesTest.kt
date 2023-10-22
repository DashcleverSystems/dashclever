package pl.dashclever.tests.integration.repairmanagment.planning.readmodel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
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
internal class PlanFindingWithinDatesTest @Autowired constructor(
    private val planRepository: PlanRepository,
    private val planReader: PlanReader
) {

    private companion object {

        @JvmStatic
        fun `test dates set`(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    LocalDate.of(2023, 1, 5),
                    LocalDate.of(2023, 1, 6)
                ),
                Arguments.of(
                    LocalDate.of(2023, 1, 6),
                    LocalDate.of(2023, 1, 6)
                ),
                Arguments.of(
                    LocalDate.of(2023, 1, 6),
                    LocalDate.of(2023, 1, 7)
                ),
                Arguments.of(
                    LocalDate.of(2023, 1, 4),
                    LocalDate.of(2023, 1, 7)
                ),
                Arguments.of(
                    LocalDate.of(2023, 1, 4),
                    LocalDate.of(2023, 1, 7)
                ),
                Arguments.of(
                    LocalDate.of(1999, 1, 1),
                    LocalDate.of(2024, 1, 1)
                )
            )
    }

    @AfterEach
    fun cleanPlanRepository() = planRepository.deleteAll()

    @Test
    fun `should not find any`() {
        // given
        val plan = PlanFactory.create(
            estimateId = UUID.randomUUID().toString(),
            jobs = mapOf(
                1L to 120,
                2L to 240
            )
        )
        plan.assign(1L, "employeeId", LocalDate.of(2023, 1, 6))
        planRepository.save(plan)

        // when
        val result: Set<PlanDto> = planReader.findByDateRange(
            from = LocalDate.of(2023, 1, 7),
            to = LocalDate.of(2023, 1, 8)
        )

        // then
        assertThat(result).isEmpty()
    }

    @ParameterizedTest
    @MethodSource("test dates set")
    fun `should find two within given dates range`() {
        // given
        val plan1 = PlanFactory.create(
            estimateId = UUID.randomUUID().toString(),
            jobs = mapOf(
                1L to 120,
                2L to 240
            )
        )
        plan1.assign(1L, "employeeId", LocalDate.of(2023, 1, 6))
        planRepository.save(plan1)
        val plan2 = PlanFactory.create(
            estimateId = UUID.randomUUID().toString(),
            jobs = mapOf(
                1L to 120,
                2L to 240
            )
        )
        plan2.assign(1L, "employeeId", LocalDate.of(2023, 1, 8))
        planRepository.save(plan2)

        // when
        val result: Set<PlanDto> = planReader.findByDateRange(LocalDate.of(2023, 1, 6), LocalDate.of(2023, 1, 8))

        // then
        assertThat(result).hasSize(2)
    }
}
