package pl.dashclever.accountresources.employee

import jakarta.persistence.Entity
import jakarta.persistence.EnumType.ORDINAL
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import pl.dashclever.commons.hibernate.BaseEntity
import java.util.UUID

@Entity
@Table(name = "EMPLOYEE")
data class Employee(
    var firstName: String,
    var lastName: String?,
    val workshopId: UUID,
    @Enumerated(ORDINAL)
    var workplace: Workplace,
) : BaseEntity<UUID>() {

    @Id
    val id: UUID = UUID.randomUUID()
    override fun getIdentifier(): UUID = this.id
}
