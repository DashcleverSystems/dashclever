package pl.dashclever.tests.integration.accountresources

import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.accountresources.account.model.Account
import java.util.UUID

@Component
internal interface AccountCleaner : Repository<Account, UUID> {

    fun deleteAll()
}
