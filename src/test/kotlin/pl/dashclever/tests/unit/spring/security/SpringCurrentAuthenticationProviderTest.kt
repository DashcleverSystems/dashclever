package pl.dashclever.tests.unit.spring.security

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import pl.dashclever.spring.security.SpringCurrentAuthenticationProvider

internal class SpringCurrentAuthenticationProviderTest {

    @Test
    fun `should provide currently authenticated user`() {
        // given
        val securityContext = mockk<SecurityContext>()
        val authentication = mockk<Authentication>()
        every { securityContext.authentication } returns authentication
        SecurityContextHolder.setContext(securityContext)
        val testee = SpringCurrentAuthenticationProvider()

        // when
        val result = testee.getAuthentication()

        // then
        assertThat(result).isEqualTo(authentication)
    }
}
