package pl.dashclever.accountresources.employee

import java.util.Optional
import java.util.UUID

interface EmployeeRepository {

    fun save(employee: Employee): Employee
    fun findById(id: UUID): Optional<Employee>
    fun findAll(): Set<Employee>
}
