package pl.dashclever.accountresources.employee

import jakarta.validation.constraints.NotBlank
import pl.dashclever.publishedlanguage.NOT_BLANK
import java.util.UUID

data class EmployeeDto(
    val id: UUID?,
    @NotBlank(message = NOT_BLANK)
    val firstName: String,
    @NotBlank(message = NOT_BLANK)
    val lastName: String?,
    val workshopId: UUID,
    val workplace: Workplace,
) {

    companion object Mapper {

        fun from(employee: Employee) =
            EmployeeDto(
                id = employee.id,
                firstName = employee.firstName,
                lastName = employee.lastName,
                workshopId = employee.workshopId,
                workplace = employee.workplace,
            )
    }

    fun modify(employee: Employee) {
        employee.firstName = this.firstName
        employee.lastName = this.lastName
        employee.workplace = this.workplace
    }
}
