package pl.dashclever.spring.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import pl.dashclever.accountresources.account.readmodel.CredentialsReader
import pl.dashclever.commons.security.Access
import java.util.UUID

class EntryUserDetailsService(
    private val credentialsReader: CredentialsReader,
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) throw UsernameNotFoundException("Not specified username!")
        val credentialsDto = credentialsReader.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("No user with $username found") }
        return EntryAccessDetails(credentialsDto.accId, credentialsDto.username, credentialsDto.password)
    }

    private data class EntryAccessDetails(
        override val accountId: UUID,
        private val username: String,
        private val passwordHash: String,
    ) : Access, UserDetails {

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
            mutableSetOf()

        override fun getPassword(): String =
            passwordHash

        override fun getUsername(): String =
            username

        override fun isAccountNonExpired(): Boolean =
            true

        override fun isAccountNonLocked(): Boolean =
            true

        override fun isCredentialsNonExpired(): Boolean =
            true

        override fun isEnabled(): Boolean =
            true
    }
}
