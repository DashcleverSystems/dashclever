package pl.dashclever.accountresources.employee.infrastrucutre.repository

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.accountresources.employee.Employee
import pl.dashclever.accountresources.employee.EmployeeRepository
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.commons.security.EntitySecurityRecordRepository
import java.util.*

@Repository
class EmployeeWorkshopSecuredRepository(
    private val entityManager: EntityManager,
    private val securityRecordRepository: EntitySecurityRecordRepository<Employee, UUID, WorkshopEmployee>,
    private val currentAccessProvider: CurrentAccessProvider,
    private val employeeWorkshopSecuredJpaRepository: EmployeeWorkshopSecuredJpaRepository
) : EmployeeRepository {

    @Transactional
    override fun save(employee: Employee): Employee {
        entityManager.persist(employee)
        if (securityRecordRepository.doesSecurityRecordExistFor(employee)) {
            return employee
        }
        val currentAccessWorkshop = currentAccessProvider.currentWorkshopId()
        securityRecordRepository.create(WorkshopEmployee(currentAccessWorkshop.workshopId, employee.id))
        return employee
    }

    override fun findById(id: UUID): Optional<Employee> {
        val currentWorkshopAccess = currentAccessProvider.currentWorkshopId()
        return employeeWorkshopSecuredJpaRepository.findById(currentWorkshopAccess.workshopId, id)
    }

    override fun findAll(): Set<Employee> {
        val currentWorkshopAccess = currentAccessProvider.currentWorkshopId()
        return employeeWorkshopSecuredJpaRepository.findAllByWorkshopId(currentWorkshopAccess.workshopId)
    }
}
