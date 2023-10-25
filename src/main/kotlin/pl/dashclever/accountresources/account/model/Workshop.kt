package pl.dashclever.accountresources.account.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import pl.dashclever.commons.hibernate.UpdateTimestampEntity
import java.util.UUID

@Entity
@Table(name = "WORKSHOP")
internal class Workshop(
    val displayName: String
) : UpdateTimestampEntity<UUID>() {

    @Id
    val id: UUID = UUID.randomUUID()
    override fun getIdentifierValue(): UUID = this.id
}
