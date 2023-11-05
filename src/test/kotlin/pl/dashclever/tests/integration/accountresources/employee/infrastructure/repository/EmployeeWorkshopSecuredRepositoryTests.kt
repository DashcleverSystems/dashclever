package pl.dashclever.tests.integration.accountresources.employee.infrastructure.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.accountresources.employee.Employee
import pl.dashclever.accountresources.employee.Workplace.LABOUR
import pl.dashclever.accountresources.employee.infrastrucutre.repository.EmployeeSecurityRecordRepository
import pl.dashclever.accountresources.employee.infrastrucutre.repository.EmployeeWorkshopSecuredRepository
import pl.dashclever.accountresources.employee.infrastrucutre.repository.WorkshopEmployee
import pl.dashclever.commons.security.Access.WithAuthorities.Authority
import pl.dashclever.commons.security.ApplicationAccessSetter
import pl.dashclever.commons.security.EntitySecurityRecordRepository
import pl.dashclever.tests.integration.TestcontainersInitializer
import pl.dashclever.tests.integration.spring.AllAuthoritiesAccess
import pl.dashclever.tests.integration.spring.TestAccess
import pl.dashclever.tests.integration.spring.TestAccessSetter
import java.util.UUID

@SpringBootTest
@Transactional
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class EmployeeWorkshopSecuredRepositoryTests(
    @Autowired private val employeeWorkshopSecuredRepository: EmployeeWorkshopSecuredRepository,
    @Autowired private val employeeSecurityRecordRepository: EntitySecurityRecordRepository<Employee, UUID, WorkshopEmployee>
) {

    @Test
    fun `should create a security record for employee`() {
        // given
        val testApplicationAccessSetter = TestAccessSetter()
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
    }

    @Test
    fun `should not find employee if he does not belong to current access's workshop`() {
    }
}
