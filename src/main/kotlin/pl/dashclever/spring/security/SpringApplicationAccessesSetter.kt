package pl.dashclever.spring.security

import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import pl.dashclever.accountresources.account.readmodel.AccessesReader
import pl.dashclever.accountresources.employee.Workplace
import pl.dashclever.accountresources.employee.Workplace.LABOUR
import pl.dashclever.accountresources.employee.Workplace.SUPERVISOR
import pl.dashclever.commons.security.Access
import pl.dashclever.commons.security.Access.WithAuthorities.Authority
import pl.dashclever.commons.security.Access.WithAuthorities.Authority.INSIGHT_REPAIR
import pl.dashclever.commons.security.Access.WithAuthorities.Authority.REPAIR_PROCESS
import pl.dashclever.commons.security.Access.WorkshopEmployeeAccess
import pl.dashclever.commons.security.Access.WorkshopOwnerAccess
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.commons.security.UserSetter
import java.lang.IllegalStateException
import java.util.UUID

@Service
class SpringApplicationAccessesSetter(
    private val accessesReader: AccessesReader,
    private val currentAccessProvider: CurrentAccessProvider,
    private val springSecurityContextProvider: SpringSecurityContextProvider,
    private val springCurrentAuthenticationProvider: SpringCurrentAuthenticationProvider,
) : UserSetter {

    override fun setOwnerAccess(workshopId: UUID): WorkshopOwnerAccess {
        val currentAccess: Access = currentAccess()
        val ownerAccesses = this.accessesReader.findWorkshopOwnerAccesses(currentAccess.accountId)
        val access = ownerAccesses.firstOrNull { it.workshopId == workshopId }
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Did not find requested access")
        val newOwnerAccess = WorkshopOwnerAccess(
            accountId = currentAccess.accountId,
            workshopId = access.workshopId
        )
        val currentAuth = springCurrentAuthenticationProvider.getAuthentication()
        springSecurityContextProvider.getSecurityContext().authentication = WorkshopAccessAuthentication(newOwnerAccess, currentAuth)
        return newOwnerAccess
    }

    private fun currentAccess(): Access = currentAccessProvider.currentAccess()
        ?: throw IllegalStateException("Could provide currently authenticated user")

    override fun setEmployeeAccess(employeeId: UUID): WorkshopEmployeeAccess {
        val currentAccess = currentAccess()
        val employeeAccesses = this.accessesReader.findEmployeeAccesses(currentAccess.accountId)
        val access = employeeAccesses.firstOrNull { it.employeeId == employeeId }
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Did not find requested access")
        val newEmployeeAccess = WorkshopEmployeeAccess(
            accountId = currentAccess.accountId,
            workshopId = access.workshopId,
            employeeId = access.employeeId,
            authorities = access.employeeWorkplace.toAuthorities()
        )
        val currentAuth = springCurrentAuthenticationProvider.getAuthentication()
        springSecurityContextProvider.getSecurityContext().authentication = WorkshopAccessAuthentication(newEmployeeAccess, currentAuth)
        return newEmployeeAccess
    }

    private fun Workplace.toAuthorities(): Set<Authority> {
        return when (this) {
            SUPERVISOR -> setOf(INSIGHT_REPAIR, REPAIR_PROCESS)
            LABOUR -> setOf(REPAIR_PROCESS)
        }
    }

    private data class WorkshopAccessAuthentication(
        private val access: Access.WithAuthorities,
        private val auth: Authentication,
    ) : Authentication {

        private var isAuth = true

        override fun getName(): String = auth.name

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
            access.authorities.map { GrantedAuthority { it.name } }.toMutableSet()

        override fun getCredentials(): Any =
            throw IllegalAccessException()

        override fun getDetails(): Any = auth.details

        override fun getPrincipal(): Any = access

        override fun isAuthenticated(): Boolean =
            this.isAuth

        override fun setAuthenticated(isAuthenticated: Boolean) {
            this.isAuth = isAuthenticated
        }
    }
}
