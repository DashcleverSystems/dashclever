package pl.dashclever.repairmanagment.plannig.readmodel.impl

import org.springframework.stereotype.Component
import pl.dashclever.repairmanagment.plannig.readmodel.EmployeeOccupationDto
import pl.dashclever.repairmanagment.plannig.readmodel.EmployeeOccupationReader
import pl.dashclever.repairmanagment.plannig.readmodel.PlanReader
import java.time.LocalDate
import java.util.UUID

@Component
class EmployeeOccupationReaderImpl(
    private val planReader: PlanReader
) : EmployeeOccupationReader {

    override fun findByEmployeeIdAt(employeeId: UUID, at: LocalDate): EmployeeOccupationDto {
        val plans = this.planReader.findByDateRange(at, at)
        val employeeJobs = plans.flatMap { it.jobs }.filter { it.assignedTo == employeeId.toString() }
        val occupationInManMinutes = employeeJobs.map { it.manMinutes }
            .reduce { acc, manMinutes -> acc + manMinutes }
        return EmployeeOccupationDto(
            employeeId = employeeId.toString(),
            manMinutes = occupationInManMinutes
        )
    }
}
