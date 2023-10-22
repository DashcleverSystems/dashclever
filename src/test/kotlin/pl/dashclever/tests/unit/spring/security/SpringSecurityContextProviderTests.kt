package pl.dashclever.tests.unit.spring.security

import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import pl.dashclever.spring.security.SpringSecurityContextProvider

internal class SpringSecurityContextProviderTests {

    @Test
    fun `should return current security context`() {
        // given
        val securityContext = mockk<SecurityContext>()
        SecurityContextHolder.setContext(securityContext)
        val testee = SpringSecurityContextProvider()

        // when
        val result = testee.getSecurityContext()

        // then
        assertThat(result).isEqualTo(securityContext)
    }
}
