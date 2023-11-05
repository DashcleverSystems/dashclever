package pl.dashclever.tests.integration.spring

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import pl.dashclever.commons.security.Access
import pl.dashclever.commons.security.Access.WithAuthorities
import pl.dashclever.commons.security.Access.WithAuthorities.Authority
import pl.dashclever.commons.security.Access.WithWorkshopId
import java.util.*

internal class TestAccessSetter {

    fun setAccess(access: TestAccess?) {
        if (access == null) {
            SecurityContextHolder.getContext().authentication = null
        } else {
            SecurityContextHolder.getContext().authentication = TestAuthentication(access)
        }
    }
}

internal val AllAuthoritiesAccess = TestAccess(
    accountId = UUID.randomUUID(),
    authorities = Authority.values().toSet(),
    workshopId = UUID.randomUUID()
)

internal data class TestAccess(
    override val accountId: UUID,
    override val authorities: Set<Authority>,
    override val workshopId: UUID
) : WithWorkshopId, WithAuthorities, Access, UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        this.authorities.map { GrantedAuthority { it.name } }.toMutableSet()

    override fun getPassword(): String = "password"

    override fun getUsername(): String = "username"

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}

private data class TestAuthentication(
    private val access: TestAccess
) : Authentication {

    private var isAuth = true

    override fun getName(): String = "UsernamePasswordAuthenticationToken"
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        access.authorities.map { GrantedAuthority { it.name } }.toMutableSet()

    override fun getCredentials(): Any {
        error("Test auth does not contain credentials")
    }

    override fun getDetails(): Any = "details"

    override fun getPrincipal(): Any = this.access

    override fun isAuthenticated(): Boolean = true
    override fun setAuthenticated(isAuthenticated: Boolean) {
        isAuth = isAuthenticated
    }
}
