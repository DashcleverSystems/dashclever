package pl.dashclever.spring.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import pl.dashclever.commons.security.ApplicationAccessSetter
import pl.dashclever.commons.security.WithAccountId
import pl.dashclever.commons.security.WithAuthorities
import pl.dashclever.commons.security.WithAuthorities.Authority
import pl.dashclever.commons.security.WithWorkshopId
import pl.dashclever.commons.security.WorkshopEmployee
import pl.dashclever.commons.security.WorkshopOwner
import java.util.UUID

@Service
class SpringApplicationAccessesSetter(
    private val springCurrentAuthenticationProvider: SpringCurrentAuthenticationProvider,
    private val springSecurityContextProvider: SpringSecurityContextProvider
) : ApplicationAccessSetter {

    override fun set(access: WorkshopEmployee): WorkshopEmployee {
        val currentAuth = springCurrentAuthenticationProvider.getAuthentication()
        val workshopEmployeeAuthentication = access.toSpringSecurityAuthentication(currentAuth)
        springSecurityContextProvider.getSecurityContext().authentication = workshopEmployeeAuthentication
        return access
    }

    private fun WorkshopEmployee.toSpringSecurityAuthentication(currentAuthentication: Authentication): Authentication {
        return AccessAuthentication(
            userDetails = UserDetails(
                this.accountId,
                this.workshopId,
                this.authorities,
                username = (currentAuthentication.principal as org.springframework.security.core.userdetails.UserDetails).username
            ),
            details = currentAuthentication.details,
            name = currentAuthentication.name
        )
    }

    override fun set(access: WorkshopOwner): WorkshopOwner {
        val currentAuth = springCurrentAuthenticationProvider.getAuthentication()
        val workshopOwnerAuthentication = access.toSpringSecurityAuthentication(currentAuth)
        springSecurityContextProvider.getSecurityContext().authentication = workshopOwnerAuthentication
        return access
    }

    private fun WorkshopOwner.toSpringSecurityAuthentication(currentAuthentication: Authentication): Authentication {
        return AccessAuthentication(
            userDetails = UserDetails(
                this.accountId,
                this.workshopId,
                this.authorities,
                username = (currentAuthentication.principal as org.springframework.security.core.userdetails.UserDetails).username
            ),
            details = currentAuthentication.details,
            name = currentAuthentication.name
        )
    }

    private data class UserDetails(
        override val accountId: UUID,
        override val workshopId: UUID,
        override val authorities: Set<Authority>,
        private val username: String
    ) : org.springframework.security.core.userdetails.UserDetails, WithWorkshopId, WithAccountId, WithAuthorities {

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
            authorities.map { GrantedAuthority { it.name } }.toMutableSet()

        override fun getPassword(): String = throw IllegalAccessException("It is not possible to access credentials of already authenticated user")

        override fun getUsername(): String = username

        override fun isAccountNonExpired(): Boolean = true

        override fun isAccountNonLocked(): Boolean = true

        override fun isCredentialsNonExpired(): Boolean = true

        override fun isEnabled(): Boolean = true
    }

    private data class AccessAuthentication(
        private val userDetails: UserDetails,
        private val details: Any?,
        private val name: String
    ) : Authentication {

        private var isAuth = true

        override fun getName(): String = name

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
            userDetails.authorities.map { GrantedAuthority { it.name } }.toMutableSet()

        override fun getCredentials(): Any = throw IllegalAccessException("It is not possible to access credentials of already authenticated user")

        override fun getDetails(): Any? = details

        override fun getPrincipal(): Any = userDetails

        override fun isAuthenticated(): Boolean =
            this.isAuth

        override fun setAuthenticated(isAuthenticated: Boolean) {
            this.isAuth = isAuthenticated
        }
    }
}
