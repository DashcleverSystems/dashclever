package pl.dashclever.accountresources.account.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import pl.dashclever.commons.hibernate.WithCreationTimestamp
import pl.dashclever.commons.hibernate.WithLastModificationTimestamp
import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "EMPLOYEESHIP")
internal data class Employeeship(
    @EmbeddedId
    val employeeship: EmployeeshipId,
) : WithLastModificationTimestamp, WithCreationTimestamp {

    @CreationTimestamp
    override val createdOn: LocalDateTime? = null

    @UpdateTimestamp
    override val lastModifiedOn: LocalDateTime? = null
}

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
