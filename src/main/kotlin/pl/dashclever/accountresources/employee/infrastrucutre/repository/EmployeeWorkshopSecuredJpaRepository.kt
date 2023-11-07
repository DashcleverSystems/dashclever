package pl.dashclever.accountresources.employee.infrastrucutre.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.accountresources.employee.Employee
import java.util.Optional
import java.util.UUID

@Component
interface EmployeeWorkshopSecuredJpaRepository : Repository<Employee, UUID> {

    @Query("SELECT e FROM Employee e INNER JOIN WorkshopEmployee sr ON e.id = sr.id.employeeId WHERE e.id = :employeeId AND sr.id.workshopId = :workshopId")
    fun findById(workshopId: UUID, employeeId: UUID): Optional<Employee>

    @Query("SELECT e FROM Employee e INNER JOIN WorkshopEmployee sr ON e.id = sr.id.employeeId WHERE sr.id.workshopId = :workshopId")
    fun findAllByWorkshopId(workshopId: UUID): Set<Employee>
}
