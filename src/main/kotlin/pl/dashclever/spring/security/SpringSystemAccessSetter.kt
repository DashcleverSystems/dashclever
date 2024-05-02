package pl.dashclever.spring.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import pl.dashclever.commons.security.SystemAccessSetter
import pl.dashclever.commons.security.SystemOnBehalfOfWorkshop
import pl.dashclever.commons.security.WithWorkshopId
import java.util.UUID

@Component
class SpringSystemAccessSetter(
    private val springSecurityContextProvider: SpringSecurityContextProvider
) : SystemAccessSetter {

    override fun set(systemAccess: SystemOnBehalfOfWorkshop) {
        val workshopEmployeeAuthentication = SystemAccessAuthentication(SystemUserDetails(systemAccess.workshopId))
        springSecurityContextProvider.getSecurityContext().authentication = workshopEmployeeAuthentication
    }

    private data class SystemUserDetails(
        override val workshopId: UUID
    ) : UserDetails, WithWorkshopId {

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableSetOf()

        override fun getPassword(): String = throw IllegalAccessException("System user has no credentials")

        override fun getUsername(): String = "system#onBehalf#workshopId#$workshopId"

        override fun isAccountNonExpired(): Boolean = true

        override fun isAccountNonLocked(): Boolean = true

        override fun isCredentialsNonExpired(): Boolean = true

        override fun isEnabled(): Boolean = true
    }

    private data class SystemAccessAuthentication(
        private val userDetails: UserDetails
    ) : Authentication {

        private var isAuth = true

        override fun getName(): String = "UsernamePasswordAuthenticationToken"

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
            userDetails.authorities

        override fun getCredentials(): Any = throw IllegalAccessException("System user has no credentials")

        override fun getDetails(): Any? = null

        override fun getPrincipal(): Any = userDetails

        override fun isAuthenticated(): Boolean =
            this.isAuth

        override fun setAuthenticated(isAuthenticated: Boolean) {
            this.isAuth = isAuthenticated
        }
    }
}
