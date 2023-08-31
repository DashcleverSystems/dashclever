package pl.dashclever.accountresources.account.readmodel.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import pl.dashclever.accountresources.account.readmodel.AccessDto
import pl.dashclever.accountresources.account.readmodel.AccessesReader
import pl.dashclever.accountresources.account.readmodel.Role
import pl.dashclever.accountresources.account.readmodel.Role.OWNER
import pl.dashclever.accountresources.account.readmodel.WorkshopAccessesDto
import pl.dashclever.accountresources.employee.Workplace
import java.util.UUID

@Component
class AccessesReaderImpl(
    private val jdbcTemplate: JdbcTemplate
) : AccessesReader {

    override fun findAccountAccesses(accountId: UUID): Set<AccessDto> =
        workshopOwnerAccesses(accountId).plus(employeeAccesses(accountId))

    override fun findAccountWorkshopAccesses(accountId: UUID): Set<WorkshopAccessesDto> {
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

    private fun employeeAccesses(accountId: UUID): Set<AccessDto> {
        val query = """
            SELECT
                    emp.id,
                    emp.first_name,
                    emp.workplace,
                    wrkp.id,
                    wrkp.display_name
            FROM
                    ACCOUNT acc
            INNER JOIN EMPLOYEESHIP empship ON acc.id = empship.account_id
            INNER JOIN EMPLOYEE emp ON empship.employee_id = emp.id
            INNER JOIN WORKSHOP wrkp ON emp.workshop_id = wrkp.id
            WHERE acc.id = ?
        """.trimIndent()
        return jdbcTemplate.query(
            query,
            { rs, _ ->
                AccessDto(
                    workshopId = UUID.fromString(rs.getString("workshop_id")),
                    workshopName = rs.getString("display_name"),
                    employeeId = UUID.fromString(rs.getString("id")),
                    employeeFirstName = rs.getString("first_name"),
                    isOwnerAccess = false,
                    authorities = Role.from(Workplace.valueOf(rs.getString("workplace"))).authorities
                )
            },
            accountId
        ).toSet()
    }

    private fun workshopOwnerAccesses(accountId: UUID): Set<AccessDto> {
        val query = """
            SELECT
                    ws.id,
                    ws.display_name
            FROM
                    WORKSHOP ws
            WHERE
                    ws.owner_account_id = ?
        """.trimIndent()
        return jdbcTemplate.query(
            query,
            { rs, _ ->
                AccessDto(
                    workshopId = UUID.fromString(rs.getString("id")),
                    workshopName = rs.getString("display_name"),
                    isOwnerAccess = true,
                    employeeId = null,
                    employeeFirstName = null,
                    authorities = OWNER.authorities
                )
            },
            accountId
        ).toSet()
    }
}
