package pl.dashclever.tests.integration.repairmanagment.planning.readmodel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.dashclever.repairmanagment.plannig.model.PlanFactory
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.plannig.readmodel.EmployeeOccupationReader
import pl.dashclever.tests.integration.TestcontainersInitializer
import java.time.LocalDate
import java.util.UUID

@SpringBootTest
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class EmployeeOccupationReaderTest(
	@Autowired private val planRepository: PlanRepository,
	@Autowired private val employeeOccupationReader: EmployeeOccupationReader
) {

	@Test
	fun `should return information about employee occupation at given day`() {
		// given
		val plan = PlanFactory.create(
			estimateId = UUID.randomUUID().toString(),
			jobs = mapOf(
				1L to 60,
				2L to 40
			)
		)
		plan.assign(1L, "employeeId", LocalDate.of(2020, 2, 2))
		plan.assign(2L, "employeeId", LocalDate.of(2020, 2, 2))
		planRepository.save(plan)

		// when
		val result = employeeOccupationReader.findByEmployeeIdAt("employeeId", LocalDate.of(2020, 2, 2))

		// then
		assertThat(result).hasValueSatisfying {
			assertThat(it.employeeId).isEqualTo("employeeId")
			assertThat(it.manMinutes).isEqualTo(100)
		}
	}
}