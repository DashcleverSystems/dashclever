package pl.dashclever.accountresources.account.readmodel

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.accountresources.account.model.Account
import java.util.Optional
import java.util.UUID

@Component
interface WorkshopReader : Repository<Account, UUID> {

    @Query(
        value = "SELECT id, display_name as displayName FROM WORKSHOP w WHERE w.id = :id",
        nativeQuery = true
    )
    fun findById(id: UUID): Optional<WorkshopDto>
}
interface WorkshopDto {
    val id: UUID
    val displayName: String
}
