package pl.dashclever.accountresources.employee

import org.springframework.data.repository.Repository
import java.util.Optional
import java.util.UUID

interface EmployeeRepository : Repository<Employee, UUID> {

    fun save(employee: Employee): Employee
    fun findById(id: UUID): Optional<Employee>
    fun findByWorkshopId(workshopId: UUID): Set<Employee>
}
