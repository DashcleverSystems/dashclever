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
import java.util.stream.Stream

private const val PATH = "/api/planning"

@RestController
@RequestMapping(PATH, produces = [MediaType.APPLICATION_JSON_VALUE])
@Tag(name = "planning-api")
internal class EmployeeOccupationReadRestApi(
    private val employeeOccupationReader: EmployeeOccupationReader,
) {

    @GetMapping("/{planId}/employee/{employeeId}/occupation")
    fun getOccupation(
        @PathVariable planId: UUID,
        @PathVariable employeeId: UUID,
        @RequestParam("at") at: LocalDate,
    ): EmployeeOccupationDto =
        Stream.of(
            employeeOccupationReader.findByEmployeeId(employeeId.toString(), at),
            employeeOccupationReader.findByPlanIdAndEmployeeId(planId, employeeId.toString(), at)
        )
            .filter { it.isPresent }
            .map { it.get() }
            .reduce { acc: EmployeeOccupationDto, next: EmployeeOccupationDto -> acc + next }
            .orElseGet { NotOccupiedEmployee(employeeId.toString()) }

    @GetMapping("/{planId}/employee/occupation")
    fun getOccupation(
        @PathVariable planId: UUID,
        @RequestParam("at") at: LocalDate,
    ): List<EmployeeOccupationDto> = (employeeOccupationReader.findAll(at) + employeeOccupationReader.findAllByPlanId(planId, at)).toList()
}

private data class NotOccupiedEmployee(
    override val employeeId: String,
) : EmployeeOccupationDto {

    override val manMinutes: Int = 0
}

