package pl.dashclever.spring.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.commons.security.WithAccountId
import pl.dashclever.commons.security.WithWorkshopId

@Component
class SpringCurrentAccessProvider : CurrentAccessProvider {

    override fun currentAccountId(): WithAccountId {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentPrincipal = authentication.principal
        return when (currentPrincipal) {
            is WithAccountId -> authentication.principal as WithAccountId
            else -> error("Could not provide currently authenticated account id. Current authentication principal is of unknown type.")
        }
    }

    override fun currentWorkshopId(): WithWorkshopId {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentPrincipal = authentication.principal
        return when (currentPrincipal) {
            is WithWorkshopId -> authentication.principal as WithWorkshopId
            else -> error("Could not provide currently authenticated workshop id. Current authentication principal is of unknown type.")
        }
    }
}
