package pl.dashclever.accountresources.account.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.io.Serializable
import java.util.UUID

@Entity
@Table(name = "EMPLOYEESHIP")
internal data class Employeeship(
    @EmbeddedId
    val employeeship: EmployeeshipId
)

@Embeddable
internal data class EmployeeshipId(
    @Column(name = "account_id")
    val accountId: UUID,
    @Column(name = "employee_id")
    val employeeId: UUID
) : Serializable {

    companion object {

        const val serialVersionUID = 42L
    }
}
