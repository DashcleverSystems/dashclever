package pl.dashclever.tests.unit.spring.security

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import pl.dashclever.commons.security.WithAccountId
import pl.dashclever.commons.security.WithAuthorities.Authority.REPAIR_PROCESS
import pl.dashclever.commons.security.WorkshopEmployee
import pl.dashclever.commons.security.WorkshopOwner
import pl.dashclever.spring.security.SpringCurrentAccessProvider
import java.lang.IllegalStateException
import java.util.UUID

internal class SpringCurrentAccountIdProviderTest {

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @ParameterizedTest
    @MethodSource("provide valid accesses")
    fun `should return current accountId`(accessWithType: AccessWithType) {
        // given
        val securityContext = mockk<SecurityContext>()
        val authentication = mockk<Authentication>()
        every { securityContext.authentication } returns authentication
        every { authentication.principal } returns accessWithType.first
        SecurityContextHolder.setContext(securityContext)
        val testee = SpringCurrentAccessProvider()

        // when
        val result: WithAccountId = testee.currentAccountId()

        // then
        assertThat(result).isNotNull
        assertThat(result).isEqualTo(accessWithType.first)
        assertThat(result::class.java).isEqualTo(accessWithType.second)
    }

    @Test
    fun `should throw error when current authentication contains unknown type of access`() {
        // given
        val securityContext = mockk<SecurityContext>()
        val authentication = mockk<Authentication>()
        every { securityContext.authentication } returns authentication
        data class AuthPrincipal(val randomValue: String)
        every { authentication.principal } returns AuthPrincipal("test")
        SecurityContextHolder.setContext(securityContext)
        val testee = SpringCurrentAccessProvider()

        // when
        val result = assertThrows<IllegalStateException> { testee.currentAccountId() }

        // then
        assertThat(result).hasMessage("Could not provide currently authenticated account id. Current authentication principal is of unknown type.")
    }

    private companion object {

        @JvmStatic
        fun `provide valid accesses`(): List<AccessWithType> {
            return listOf(
                WorkshopOwner(UUID.randomUUID(), UUID.randomUUID()) to WorkshopOwner::class.java,
                WorkshopEmployee(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), setOf(REPAIR_PROCESS)) to WorkshopEmployee::class.java
            )
        }
    }
}

private typealias AccessWithType = Pair<WithAccountId, Class<*>>
