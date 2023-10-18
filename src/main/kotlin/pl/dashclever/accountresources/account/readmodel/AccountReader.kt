package pl.dashclever.accountresources.account.readmodel

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.accountresources.account.model.Account
import java.util.Optional
import java.util.UUID

@Component
interface AccountReader : Repository<Account, UUID> {

    @Query(
        value = "SELECT id, username, email FROM ACCOUNT acc WHERE acc.username = :username",
        nativeQuery = true,
    )
    fun findByUsername(username: String): Optional<AccountDto>
}

interface AccountDto {

    val id: UUID
    val username: String
    val email: String
}
