package pl.dashclever.accountresources.account.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import pl.dashclever.commons.hibernate.BaseEntity
import java.util.UUID

@Entity
@Table(name = "WORKSHOP")
internal class Workshop(
    val displayName: String,
) : BaseEntity<UUID>() {

    @Id
    val id: UUID = UUID.randomUUID()
    override fun getIdentifier(): UUID = this.id
}
