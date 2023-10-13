package pl.dashclever.accountresources.account.infrastructure

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.dashclever.accountresources.account.model.Account
import pl.dashclever.accountresources.account.model.AccountRepository

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun registerAccount(username: String, password: String, email: String): Account {
        val passwordHash = passwordEncoder.encode(password)
        return accountRepository.save(Account(username, passwordHash, email))
    }
}
