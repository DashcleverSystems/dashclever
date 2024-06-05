package pl.dashclever.accountresources.employee

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.net.URI
import java.util.UUID

private const val PATH = "api"

@RestController
@RequestMapping(PATH)
@Tag(name = "employee-api")
internal class EmployeeRestApi(
    private val employeeRepository: EmployeeRepository
) {

    @PostMapping("/employee")
    @Transactional
    fun addEmployee(
        @RequestBody dto: EmployeeDto
    ): ResponseEntity<EmployeeDto> {
        val employee = employeeRepository.save(
            Employee(
                firstName = dto.firstName,
                lastName = dto.lastName,
                workplace = dto.workplace
            )
        )
        return ResponseEntity.created(URI.create("$PATH/${employee.id}"))
            .body(EmployeeDto.from(employee))
    }

    @PutMapping("/employee/{employeeId}")
    @Transactional
    fun changeEmployee(
        @PathVariable employeeId: UUID,
        @RequestBody dto: EmployeeDto
    ): ResponseEntity<EmployeeDto> {
        val employee = employeeRepository.findById(employeeId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Not found") }
        dto.modify(employee)
        employeeRepository.save(employee)
        return ResponseEntity.accepted().contentType(MediaType.APPLICATION_JSON).body(EmployeeDto.from(employee))
    }

    @GetMapping("/employee", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll(
        @RequestParam workplace: Workplace?
    ): List<EmployeeDto> {
        var result = employeeRepository.findAll()
        if (workplace != null) {
            result = result.filter { it.workplace == workplace }.toSet()
        }
        return result.map { EmployeeDto.from(it) }
    }
}
