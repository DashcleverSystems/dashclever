package pl.dashclever.accountresources.account.infrastructure.rest.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

internal data class RegisterReq(
    @field:Size(min = 8, max = 24)
    val username: String,
    @field:Size(min = 8, max = 128)
    val password: String,
    @field:Email
    val email: String
)
