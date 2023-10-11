package pl.dashclever.accountresources.account.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import pl.dashclever.commons.hibernate.EntityBase
import java.util.UUID

@Entity
@Table(name = "WORKSHOP")
internal data class Workshop(
    val displayName: String,
) : EntityBase<UUID>() {
    @Id
    override val id: UUID = UUID.randomUUID()
}
