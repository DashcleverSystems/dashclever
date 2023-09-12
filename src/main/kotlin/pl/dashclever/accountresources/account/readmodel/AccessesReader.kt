package pl.dashclever.accountresources.account.readmodel

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.accountresources.account.model.Account
import pl.dashclever.accountresources.account.readmodel.Role.EMPLOYEE
import pl.dashclever.accountresources.account.readmodel.Role.OWNER
import pl.dashclever.accountresources.employee.Workplace
import java.util.UUID

@Component
interface AccessesReader : Repository<Account, UUID> {

    fun findAccountAccesses(accountId: UUID): Set<AccessDto> {
        val employeeAccesses = this.findEmployeeAccesses(accountId).map {
            AccessDto(
                workshopId = it.workshopId,
                workshopName = it.workshopName,
                isOwnerAccess = false,
                employeeId = it.employeeId,
                employeeFirstName = it.employeeFirstName,
                authorities = EMPLOYEE.authorities
            )
        }
        val ownerAccesses = this.findWorkshopOwnerAccesses(accountId).map {
            AccessDto(
                workshopId = it.workshopId,
                workshopName = it.workshopName,
                isOwnerAccess = true,
                employeeId = null,
                employeeFirstName = null,
                authorities = OWNER.authorities
            )
        }
        return employeeAccesses.plus(ownerAccesses).toSet()
    }
    fun findAccountWorkshopAccesses(accountId: UUID): Set<WorkshopAccessesDto> {
        val accountAccesses: MutableSet<AccessDto> = this.findAccountAccesses(accountId).toMutableSet()
        val workshopAccessesSet: MutableSet<WorkshopAccessesDto> = mutableSetOf()
        for (access in accountAccesses) {
            val workshopAccesses = workshopAccessesSet.firstOrNull { it.workshopId == access.workshopId }
            if (workshopAccesses == null) {
                workshopAccessesSet.add(
                    WorkshopAccessesDto(access.workshopId, access.workshopName, setOf(access))
                )
            } else {
                val newAccesses = workshopAccesses.accesses.plus(access)
                val newWorkshopAccesses = workshopAccesses.copy(accesses = newAccesses)
                workshopAccessesSet.remove(workshopAccesses)
                workshopAccessesSet.add(newWorkshopAccesses)
            }
        }
        return workshopAccessesSet
    }

    @Query(
        value = """
            SELECT
                emp.id as employeeId,
                emp.first_name as employeeFirstName,
                emp.workplace as employeeWorkplace,
                wrkp.id as workshopId,
                wrkp.display_name as workshopName
            FROM
                ACCOUNT acc
            INNER JOIN EMPLOYEESHIP empship ON acc.id = empship.account_id
            INNER JOIN EMPLOYEE emp ON empship.employee_id = emp.id
            INNER JOIN WORKSHOP wrkp ON emp.workshop_id = wrkp.id
            WHERE acc.id = :accountId
        """,
        nativeQuery = true
    )
    fun findEmployeeAccesses(accountId: UUID): Set<EmployeeAccessDto>

    interface EmployeeAccessDto {
        val workshopId: UUID
        val workshopName: String
        val employeeId: UUID
        val employeeFirstName: String
        val employeeWorkplace: Workplace
    }

    @Query(
        value = "SELECT ws.id as workshopId, ws.display_name as workshopName FROM WORKSHOP ws WHERE ws.owner_account_id = :accountId",
        nativeQuery = true
    )
    fun findWorkshopOwnerAccesses(accountId: UUID): Set<OwnerAccessDto>
}

interface OwnerAccessDto {
    val workshopId: UUID
    val workshopName: String
}
