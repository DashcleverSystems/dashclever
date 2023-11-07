package pl.dashclever.accountresources.employee

import jakarta.persistence.Entity
import jakarta.persistence.EnumType.ORDINAL
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import pl.dashclever.commons.hibernate.OptimisticLockEntity
import java.util.UUID

@Entity
@Table(name = "EMPLOYEE")
data class Employee(
    var firstName: String,
    var lastName: String?,
    @Enumerated(ORDINAL)
    var workplace: Workplace
) : OptimisticLockEntity<UUID>() {

    @Id
    val id: UUID = UUID.randomUUID()
    override fun getIdentifierValue(): UUID = this.id
}
