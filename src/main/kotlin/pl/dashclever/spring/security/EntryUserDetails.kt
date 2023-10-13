package pl.dashclever.spring.security

import org.springframework.security.core.GrantedAuthority
import java.util.UUID

data class EntryUserDetails(
    override val id: UUID,
    private val username: String,
    private val passwordHash: String,
) : IdUserDetails {

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
