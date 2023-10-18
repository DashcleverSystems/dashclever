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
        value = "SELECT id AS accId, username AS username, password_hash AS password FROM ACCOUNT acc WHERE acc.username = :username",
        nativeQuery = true,
    )
    fun findByUsername(username: String): Optional<CredentialsDto>
}

interface CredentialsDto {

    val accId: UUID
    val username: String
    val password: String
}
