package pl.dashclever.spring.security

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import pl.dashclever.accountresources.account.readmodel.AccessesReader
import pl.dashclever.accountresources.employee.Workplace
import pl.dashclever.accountresources.employee.Workplace.LABOUR
import pl.dashclever.accountresources.employee.Workplace.SUPERVISOR
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.commons.security.WithAccountId
import pl.dashclever.commons.security.WithAuthorities.Authority
import pl.dashclever.commons.security.WithAuthorities.Authority.INSIGHT_REPAIR
import pl.dashclever.commons.security.WithAuthorities.Authority.REPAIR_PROCESS
import pl.dashclever.commons.security.WorkshopEmployee
import pl.dashclever.commons.security.WorkshopOwner
import java.util.UUID

@Component
class SpringSecurityApplicationFacade(
    private val accessesReader: AccessesReader,
    private val currentAccessProvider: CurrentAccessProvider,
    private val springApplicationAccessesSetter: SpringApplicationAccessesSetter
) {
    fun setOwnerAccess(workshopId: UUID): WorkshopOwner {
        val currentAccountId: WithAccountId = this.currentAccessProvider.currentAccountId()
        val ownerAccesses = this.accessesReader.findWorkshopOwnerAccesses(currentAccountId.accountId)
        val access = ownerAccesses.firstOrNull { it.workshopId == workshopId }
            ?: throw ResponseStatusException(BAD_REQUEST, "Did not find requested access")
        val newOwnerAccess = WorkshopOwner(
            accountId = currentAccountId.accountId,
            workshopId = access.workshopId
        )
        return this.springApplicationAccessesSetter.set(newOwnerAccess)
    }

    fun setEmployeeAccess(employeeId: UUID): WorkshopEmployee {
        val currentAccess = this.currentAccessProvider.currentAccountId()
        val employeeAccesses = this.accessesReader.findEmployeeAccesses(currentAccess.accountId)
        val access = employeeAccesses.firstOrNull { it.employeeId == employeeId }
            ?: throw ResponseStatusException(BAD_REQUEST, "Did not find requested access")
        val newEmployeeAccess = WorkshopEmployee(
            accountId = currentAccess.accountId,
            workshopId = access.workshopId,
            employeeId = access.employeeId,
            authorities = access.employeeWorkplace.toAuthorities()
        )
        return this.springApplicationAccessesSetter.set(newEmployeeAccess)
    }

    private fun Workplace.toAuthorities(): Set<Authority> {
        return when (this) {
            SUPERVISOR -> setOf(INSIGHT_REPAIR, REPAIR_PROCESS)
            LABOUR -> setOf(REPAIR_PROCESS)
        }
    }
}
