package pl.dashclever.spring.security

import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

interface IdUserDetails : UserDetails {
    val id: UUID
}
