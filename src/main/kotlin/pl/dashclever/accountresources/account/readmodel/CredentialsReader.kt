package pl.dashclever.accountresources.account.readmodel

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.accountresources.account.model.Account
import java.util.Optional
import java.util.UUID

@Component
interface CredentialsReader : Repository<Account, UUID> {

    @Query(
        value = "SELECT id as accId, username as username, password_hash as password FROM ACCOUNT acc WHERE acc.username = :username",
        nativeQuery = true
    )
    fun findByUsername(username: String): Optional<CredentialsDto>
}

interface CredentialsDto {
    val accId: UUID
    val username: String
    val password: String
}
