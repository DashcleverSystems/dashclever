package pl.dashclever.spring.security

import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import pl.dashclever.accountresources.account.readmodel.AccessDto
import pl.dashclever.accountresources.account.readmodel.AccessesReader
import java.util.UUID

@Service
class WorkshopUserDetailsService(
    private val accessesReader: AccessesReader,
) {

    fun workshopSpecificUserOfAuthentication(workshopId: UUID, authentication: Authentication): WorkshopUserDetails {
        val currentUser: IdUserDetails = authentication.principal as IdUserDetails?
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Could not set workshop specific")
        val accesses: Set<AccessDto> = this.accessesReader.findAccountAccesses(currentUser.id)
        val access = accesses.firstOrNull { it.workshopId == workshopId }
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Did not find requested access")
        return WorkshopUserDetails(
            id = currentUser.id,
            username = currentUser.username,
            workshopId = access.workshopId,
            employeeId = access.employeeId,
            authorities = access.authorities
        )
    }

    fun employeeSpecificUserOfAuthentication(employeeId: UUID, authentication: Authentication): WorkshopUserDetails {
        val currentUser: IdUserDetails = authentication.principal as IdUserDetails?
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Could not set workshop specific")
        val accesses: Set<AccessDto> = this.accessesReader.findAccountAccesses(currentUser.id)
        val access = accesses.firstOrNull { it.employeeId == employeeId }
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Did not find requested access")
        return WorkshopUserDetails(
            id = currentUser.id,
            username = currentUser.username,
            workshopId = access.workshopId,
            employeeId = access.employeeId,
            authorities = access.authorities
        )
    }
}
