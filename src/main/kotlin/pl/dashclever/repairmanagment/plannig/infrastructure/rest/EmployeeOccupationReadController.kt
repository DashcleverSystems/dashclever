package pl.dashclever.repairmanagment.plannig.infrastructure.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.dashclever.repairmanagment.plannig.readmodel.EmployeeOccupationDto
import pl.dashclever.repairmanagment.plannig.readmodel.EmployeeOccupationReader
import java.time.LocalDate
import java.util.UUID

private const val PATH = "/api/employee"

@RestController
@RequestMapping(PATH)
internal class EmployeeOccupationReadController(
    private val employeeOccupationReader: EmployeeOccupationReader
) {

    @GetMapping("/{employeeId}/occupation")
    fun getOccupation(
        @PathVariable employeeId: UUID,
        @RequestParam("at") at: LocalDate
    ): EmployeeOccupationDto =
        employeeOccupationReader.findByEmployeeIdAt(employeeId, at)
}
