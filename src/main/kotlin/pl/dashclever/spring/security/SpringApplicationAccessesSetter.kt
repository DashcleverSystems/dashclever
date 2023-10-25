package pl.dashclever.spring.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import pl.dashclever.commons.security.Access
import pl.dashclever.commons.security.Access.WorkshopEmployeeAccess
import pl.dashclever.commons.security.Access.WorkshopOwnerAccess
import pl.dashclever.commons.security.ApplicationAccessSetter

@Service
class SpringApplicationAccessesSetter(
    private val springCurrentAuthenticationProvider: SpringCurrentAuthenticationProvider,
    private val springSecurityContextProvider: SpringSecurityContextProvider
) : ApplicationAccessSetter {

    override fun set(access: WorkshopEmployeeAccess): WorkshopEmployeeAccess {
        val currentAuth = springCurrentAuthenticationProvider.getAuthentication()
        springSecurityContextProvider.getSecurityContext().authentication = AccessAuthentication(
            accessUserDetails = AccessUserDetails(access, currentAuth),
            auth = currentAuth
        )
        return access
    }

    override fun set(access: WorkshopOwnerAccess): WorkshopOwnerAccess {
        val currentAuth = springCurrentAuthenticationProvider.getAuthentication()
        springSecurityContextProvider.getSecurityContext().authentication = AccessAuthentication(
            accessUserDetails = AccessUserDetails(access, currentAuth),
            auth = currentAuth
        )
        return access
    }

    private data class AccessUserDetails(
        override val access: Access.WithAuthorities,
        private val auth: Authentication
    ) : UserDetails, WithAccess {

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
            access.authorities.map { GrantedAuthority { it.name } }.toMutableSet()

        override fun getPassword(): String = throw IllegalAccessException("It is not possible to access credentials of already authenticated user")

        override fun getUsername(): String = (auth.principal as UserDetails).username

        override fun isAccountNonExpired(): Boolean = true

        override fun isAccountNonLocked(): Boolean = true

        override fun isCredentialsNonExpired(): Boolean = true

        override fun isEnabled(): Boolean = true
    }

    private data class AccessAuthentication(
        private val accessUserDetails: AccessUserDetails,
        private val auth: Authentication
    ) : Authentication {

        private var isAuth = true

        override fun getName(): String = auth.name

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
            accessUserDetails.authorities

        override fun getCredentials(): Any = throw IllegalAccessException("It is not possible to access credentials of already authenticated user")

        override fun getDetails(): Any = auth.details

        override fun getPrincipal(): Any = accessUserDetails

        override fun isAuthenticated(): Boolean =
            this.isAuth

        override fun setAuthenticated(isAuthenticated: Boolean) {
            this.isAuth = isAuthenticated
        }
    }
}
