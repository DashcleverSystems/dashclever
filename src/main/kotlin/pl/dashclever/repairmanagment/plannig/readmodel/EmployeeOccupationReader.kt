package pl.dashclever.repairmanagment.plannig.readmodel

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.plannig.model.Plan
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@Component
interface EmployeeOccupationReader : Repository<Plan, UUID> {
    @Query(
        value = """
            SELECT j.assigned_to as employeeId, sum(j.man_minutes) as manMinutes
            FROM RM_PLANNING_JOB j 
            WHERE j.assigned_at = :at AND j.assigned_to = :employeeId GROUP BY j.assigned_to
            """,
        nativeQuery = true
    )
    fun findByEmployeeIdAt(employeeId: String, at: LocalDate): Optional<EmployeeOccupationDto>
}
