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
    SELECT j.assignedTo AS employeeId, SUM(j.manMinutes) AS manMinutes
    FROM Plan p
    JOIN p.jobs j
    WHERE j.assignedAt = :at
    AND j.assignedTo = :employeeId
    AND p.hasRunningRepair = TRUE
    GROUP BY j.assignedTo
    """
    )
    fun findByEmployeeId(employeeId: String, at: LocalDate): Optional<EmployeeOccupationDto>

    @Query(
        value = """
    SELECT j.assignedTo AS employeeId, SUM(j.manMinutes) AS manMinutes
    FROM Plan p
    JOIN p.jobs j
    WHERE j.assignedAt = :at
    AND p.hasRunningRepair = TRUE
    GROUP BY j.assignedTo
    """
    )
    fun findAll(at: LocalDate): Set<EmployeeOccupationDto>

    @Query(
        value = """
    SELECT j.assignedTo AS employeeId, SUM(j.manMinutes) AS manMinutes
    FROM Plan p
    JOIN p.jobs j
    WHERE j.assignedAt = :at
    AND p.id = :planningId
    GROUP BY j.assignedTo
    """
    )
    fun findAllByPlanId(planningId: UUID, at: LocalDate): Set<EmployeeOccupationDto>

    @Query(
        value = """
    SELECT j.assignedTo AS employeeId, SUM(j.manMinutes) AS manMinutes
    FROM Plan p
    JOIN p.jobs j
    WHERE j.assignedAt = :at
    AND p.id = :planningId
    AND j.assignedTo = :employeeId
    """
    )
    fun findByPlanIdAndEmployeeId(planningId: UUID, employeeId: String, at: LocalDate): Optional<EmployeeOccupationDto>
}

interface EmployeeOccupationDto {

    val employeeId: String
    val manMinutes: Int

    operator fun plus(another: EmployeeOccupationDto): EmployeeOccupationDto {
        check(another.employeeId == this.employeeId) {
            "Could not add $this to $another. You can only add occupation times of the same employee to each other."
        }
        return object : EmployeeOccupationDto {
            override val employeeId: String =
                this@EmployeeOccupationDto.employeeId
            override val manMinutes: Int =
                this@EmployeeOccupationDto.manMinutes + another.manMinutes
        }
    }
}
