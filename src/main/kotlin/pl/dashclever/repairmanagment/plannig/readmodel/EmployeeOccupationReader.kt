package pl.dashclever.repairmanagment.plannig.readmodel

import java.time.LocalDate
import java.util.UUID

interface EmployeeOccupationReader {
    fun findByEmployeeIdAt(employeeId: UUID, at: LocalDate): EmployeeOccupationDto
}
