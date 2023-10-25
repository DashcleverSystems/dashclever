package pl.dashclever.spring.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import pl.dashclever.commons.security.Access
import pl.dashclever.commons.security.CurrentAccessProvider

@Component
class SpringCurrentAccessProvider : CurrentAccessProvider {

    override fun currentAccess(): Access {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentPrincipal = authentication.principal
        return when (currentPrincipal) {
            is Access -> authentication.principal as Access
            is WithAccess -> (authentication.principal as WithAccess).access
            else -> error("Could not provide current access. Current authentication principal is of unknown type.")
        }
    }
}
