package pl.dashclever.accountresources.account.model

import java.util.UUID

sealed interface AccountEvent {

    val accountId: UUID
}

data class AccountCreatedWorkshop(
    override val accountId: UUID,
    val workshopId: UUID,
) : AccountEvent

data class AddedEmployeeship(
    override val accountId: UUID,
    val employeeId: UUID,
) : AccountEvent
