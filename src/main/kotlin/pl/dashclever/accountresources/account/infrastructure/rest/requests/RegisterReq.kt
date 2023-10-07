package pl.dashclever.accountresources.account.infrastructure.rest.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import pl.dashclever.publishedlanguage.EMAIL
import pl.dashclever.publishedlanguage.SIZE_BETWEEN

internal data class RegisterReq(
    @field:Size(min = 8, max = 24, message = "$SIZE_BETWEEN;8;24")
    val username: String,
    @field:Size(min = 8, max = 128, message = "$SIZE_BETWEEN;8;128")
    val password: String,
    @field:Email(message = EMAIL)
    val email: String,
)
