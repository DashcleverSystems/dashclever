package pl.dashclever.spring.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import pl.dashclever.commons.security.Access
import pl.dashclever.commons.security.CurrentAccessProvider

@Component
class SpringCurrentAccessProvider : CurrentAccessProvider {

    override fun currentAccess(): Access? {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.principal as? Access
    }
}
