package pl.dashclever.spring.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import pl.dashclever.accountresources.account.readmodel.AccountReader

class EntryUserDetailsService(
    private val accountReader: AccountReader
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) throw UsernameNotFoundException("Not specified username!")
        val accDto = accountReader.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("No user with $username found") }
        return EntryUserDetails(accDto.id, accDto.username, accDto.passwordHash)
    }
}
