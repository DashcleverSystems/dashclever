package pl.dashclever.accountresources.account.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.util.UUID

@Entity
@Table(name = "WORKSHOP")
internal data class Workshop(
    val displayName: String
) {
    @Id
    val id: UUID = UUID.randomUUID()
    @Version @Suppress("UnusedPrivateMember")
    private var version = 0
}
