package pl.dashclever.accountresources.account.model

import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
interface AccountRepository : Repository<Account, UUID> {

    fun save(account: Account): Account
    fun findByUsername(login: String): Optional<Account>
    fun findById(id: UUID): Optional<Account>
    fun delete(account: Account)
}
