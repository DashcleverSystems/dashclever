package pl.dashclever.repairmanagment.plannig.readmodel

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.plannig.model.Plan
import java.time.LocalDate
import java.util.*

@Component
interface EmployeeOccupationReader : Repository<Plan, UUID> {

    @Query(
        value = """
            SELECT j.assignedTo AS employeeId, SUM(j.manMinutes) AS manMinutes
            FROM RM_PLANNING_JOB j
            WHERE j.assignedAt = :at AND j.assignedTo = :employeeId GROUP BY j.assignedTo
            """
    )
    fun findByEmployeeIdAt(employeeId: String, at: LocalDate): Optional<EmployeeOccupationDto>

    @Query(value = "SELECT j.assignedTo AS employeeId, SUM(j.manMinutes) AS manMinutes FROM RM_PLANNING_JOB j WHERE j.assignedAt = :at GROUP BY j.assignedTo")
    fun findAll(at: LocalDate): Set<EmployeeOccupationDto>

}

interface EmployeeOccupationDto {

    val employeeId: String
    val manMinutes: Int
}
