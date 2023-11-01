package pl.dashclever.accountresources.employee.infrastrucutre.repository

import pl.dashclever.accountresources.employee.Employee
import pl.dashclever.accountresources.employee.EmployeeRepository
import java.util.*

class EmployeeWorkshopSecuredRepository : EmployeeRepository {

    override fun save(employee: Employee): Employee {
        TODO("Not yet implemented")
    }

    override fun findById(id: UUID): Optional<Employee> {
        TODO("Not yet implemented")
    }

    override fun findByWorkshopId(workshopId: UUID): Set<Employee> {
        TODO("Not yet implemented")
    }

}
