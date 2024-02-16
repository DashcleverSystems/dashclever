package pl.dashclever.repairmanagment.plannig.infrastructure.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.dashclever.repairmanagment.plannig.readmodel.EmployeeOccupationDto
import pl.dashclever.repairmanagment.plannig.readmodel.EmployeeOccupationReader
import java.time.LocalDate
import java.util.*

private const val PATH = "/api/employee"

@RestController
@RequestMapping(PATH, produces = [MediaType.APPLICATION_JSON_VALUE])
@Tag(name = "employee-api")
internal class EmployeeOccupationReadRestApi(
    private val employeeOccupationReader: EmployeeOccupationReader
) {

    @GetMapping("/{employeeId}/occupation")
    fun getOccupation(
        @PathVariable employeeId: UUID,
        @RequestParam("at") at: LocalDate
    ): EmployeeOccupationDto =
        employeeOccupationReader.findByEmployeeIdAt(employeeId.toString(), at)
            .orElse(NotOccupiedEmployee(employeeId.toString()))

    @GetMapping("/occupation")
    fun getOccupation(
        @RequestParam("at") at: LocalDate
    ): Set<EmployeeOccupationDto> = employeeOccupationReader.findAll(at)

    private data class NotOccupiedEmployee(
        override val employeeId: String
    ) : EmployeeOccupationDto {
        override val manMinutes: Int = 0
    }
}
