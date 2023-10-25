package pl.dashclever.accountresources.account.model

import java.util.UUID

sealed interface AccountCommand {

    val accountId: UUID
}

data class CreateWorkshop(
    override val accountId: UUID,
    val displayName: String
) : AccountCommand

data class BoundEmployee(
    override val accountId: UUID,
    val employeeId: UUID
) : AccountCommand
