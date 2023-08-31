package pl.dashclever.accountresources.account.readmodel

import java.util.UUID

interface AccessesReader {

    fun findAccountAccesses(accountId: UUID): Set<AccessDto>
    fun findAccountWorkshopAccesses(accountId: UUID): Set<WorkshopAccessesDto>
}
