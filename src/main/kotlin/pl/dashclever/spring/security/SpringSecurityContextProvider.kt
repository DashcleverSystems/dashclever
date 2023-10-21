package pl.dashclever.spring.security

import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SpringSecurityContextProvider {

    fun getSecurityContext(): SecurityContext = SecurityContextHolder.getContext()
}
