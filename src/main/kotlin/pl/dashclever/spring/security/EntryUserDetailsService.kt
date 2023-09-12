package pl.dashclever.spring.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import pl.dashclever.accountresources.account.readmodel.CredentialsReader

class EntryUserDetailsService(
    private val credentialsReader: CredentialsReader
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) throw UsernameNotFoundException("Not specified username!")
        val credentialsDto = credentialsReader.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("No user with $username found") }
        return EntryUserDetails(credentialsDto.accId, credentialsDto.username, credentialsDto.password)
    }
}
