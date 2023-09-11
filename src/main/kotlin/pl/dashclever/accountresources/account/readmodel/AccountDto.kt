package pl.dashclever.accountresources.account.readmodel

import java.util.UUID

data class AccountDto(
    val id: UUID,
    val username: String,
    val passwordHash: String,
    val email: String
)
