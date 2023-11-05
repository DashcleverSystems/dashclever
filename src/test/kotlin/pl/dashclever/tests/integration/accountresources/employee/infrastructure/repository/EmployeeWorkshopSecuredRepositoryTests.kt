package pl.dashclever.tests.integration.accountresources.employee.infrastructure.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.accountresources.employee.Employee
import pl.dashclever.accountresources.employee.Workplace
import pl.dashclever.accountresources.employee.Workplace.LABOUR
import pl.dashclever.accountresources.employee.infrastrucutre.repository.EmployeeWorkshopSecuredRepository
import pl.dashclever.accountresources.employee.infrastrucutre.repository.WorkshopEmployee
import pl.dashclever.commons.security.EntitySecurityRecordRepository
import pl.dashclever.tests.integration.TestcontainersInitializer
import pl.dashclever.tests.integration.spring.AllAuthoritiesAccess
import pl.dashclever.tests.integration.spring.TestAccessSetter
import java.util.Optional
import java.util.UUID

@SpringBootTest
@Transactional
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class EmployeeWorkshopSecuredRepositoryTests(
    @Autowired private val employeeWorkshopSecuredRepository: EmployeeWorkshopSecuredRepository,
    @Autowired private val employeeSecurityRecordRepository: EntitySecurityRecordRepository<Employee, UUID, WorkshopEmployee>
) {

    private val testApplicationAccessSetter = TestAccessSetter()

    @Test
    fun `should create a security record for employee`() {
        // given
        val testAccess = AllAuthoritiesAccess
        testApplicationAccessSetter.setAccess(testAccess)
        val employee = Employee(
            firstName = "Jane",
            lastName = "Doe",
            workshopId = testAccess.workshopId,
            workplace = LABOUR
        )

        // when
        employeeWorkshopSecuredRepository.save(employee)

        // then
        assertThat(employeeSecurityRecordRepository.doesSecurityRecordExistFor(employee)).isTrue()
    }

    @Test
    fun `should only return employees belonging only to current access's workshop`() {
        // given
        val testAccess = AllAuthoritiesAccess
        testApplicationAccessSetter.setAccess(testAccess)
        employeeWorkshopSecuredRepository.save(createEmployee(testAccess.workshopId))
        employeeWorkshopSecuredRepository.save(createEmployee(testAccess.workshopId))
        employeeWorkshopSecuredRepository.save(createEmployee(testAccess.workshopId))
        val secondTestAccess = testAccess.copy(workshopId = UUID.randomUUID())
        testApplicationAccessSetter.setAccess(secondTestAccess)
        employeeWorkshopSecuredRepository.save(createEmployee(secondTestAccess.workshopId))


        // when
        val result: Set<Employee> = employeeWorkshopSecuredRepository.findAll()

        // then
        assertThat(result).hasSize(1)
    }

    @Test
    fun `should not find employee if he does not belong to current access's workshop`() {
        // given
        val testAccess = AllAuthoritiesAccess
        testApplicationAccessSetter.setAccess(testAccess)
        val employee = createEmployee(testAccess.workshopId)
        val firstAccessEmployee = employeeWorkshopSecuredRepository.save(createEmployee(testAccess.workshopId))
        val secondTestAccess = testAccess.copy(workshopId = UUID.randomUUID())
        testApplicationAccessSetter.setAccess(secondTestAccess)
        employeeWorkshopSecuredRepository.save(employee)


        // when
        val result: Optional<Employee> = employeeWorkshopSecuredRepository.findById(firstAccessEmployee.id)

        // then
        assertThat(result).isEmpty

    }

    private fun createEmployee(
        workshopId: UUID,
        firstName: String = "Jane",
        lastName: String = "Doe",
        workplace: Workplace = LABOUR
    ): Employee = Employee(firstName, lastName, workshopId, workplace)
}
