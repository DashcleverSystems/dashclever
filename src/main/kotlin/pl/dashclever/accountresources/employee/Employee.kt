package pl.dashclever.accountresources.employee

import jakarta.persistence.Entity
import jakarta.persistence.EnumType.ORDINAL
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.util.UUID

@Entity
@Table(name = "EMPLOYEE")
data class Employee(
    var firstName: String,
    var lastName: String?,
    val workshopId: UUID,
    @Enumerated(ORDINAL)
    var workplace: Workplace
) {
    @Id
    val id: UUID = UUID.randomUUID()
    @Version @Suppress("UnusedPrivateMember")
    private var version: Int = 0
}
