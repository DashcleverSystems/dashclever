package pl.dashclever.accountresources.account.infrastructure.rest.requests

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import pl.dashclever.publishedlanguage.EMAIL
import pl.dashclever.publishedlanguage.SIZE_BETWEEN
import pl.dashclever.publishedlanguage.SIZE_MIN

internal data class RegisterReq(
    @field:Size(min = 8, max = 24, message = "$SIZE_BETWEEN;8;24")
    val username: String,
    @field:Size(min = 8, max = 128, message = "$SIZE_BETWEEN;8;128")
    val password: String,
    @field:Email(message = EMAIL)
    val email: String,
    @field:Valid
    val gowno: Gowno,
)

internal data class Gowno(
    @field:Size(min = 10000, message = "$SIZE_MIN;10000")
    val gowno: String,
)
