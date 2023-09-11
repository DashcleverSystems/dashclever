package pl.dashclever.accountresources.account.readmodel

import java.util.Optional

interface AccountReader {

    fun findByUsername(username: String): Optional<AccountDto>
}
