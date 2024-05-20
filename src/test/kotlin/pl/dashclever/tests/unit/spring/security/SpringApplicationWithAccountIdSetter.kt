package pl.dashclever.tests.unit.spring.security

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.userdetails.UserDetails
import pl.dashclever.commons.security.WithAuthorities.Authority
import pl.dashclever.commons.security.WorkshopEmployee
import pl.dashclever.commons.security.WorkshopOwner
import pl.dashclever.spring.security.SpringApplicationAccessesSetter
import pl.dashclever.spring.security.SpringCurrentAuthenticationProvider
import pl.dashclever.spring.security.SpringSecurityContextProvider
import java.util.UUID

internal class SpringApplicationWithAccountIdSetter {

    @Test
    fun `should return employee access that have been set`() {
        // given
        val springCurrentAuthenticationProvider = mockk<SpringCurrentAuthenticationProvider>()
        val currentUserDetails = mockk<UserDetails>()
        every { currentUserDetails.username } returns "testUsername"
        val currentAuth = mockk<Authentication>()
        every { currentAuth.principal } returns currentUserDetails
        every { currentAuth.details } returns null
        every { currentAuth.name } returns "UsernamePasswordAuthenticationToken"
        every { springCurrentAuthenticationProvider.getAuthentication() } returns currentAuth
        val springSecurityContextProvider = spyk<SpringSecurityContextProvider>()
        val securityContext = mockk<SecurityContext>()
        every { securityContext.authentication } returns currentAuth
        every { securityContext.authentication = any() } answers { }
        every { springSecurityContextProvider.getSecurityContext() } returns securityContext
        val testee = SpringApplicationAccessesSetter(
            springCurrentAuthenticationProvider,
            springSecurityContextProvider
        )
        val access = WorkshopEmployee(
            accountId = UUID.randomUUID(),
            workshopId = UUID.randomUUID(),
            employeeId = UUID.randomUUID(),
            authorities = emptySet()
        )

        // when
        val result = assertDoesNotThrow { testee.set(access) }

        // then
        assertThat(result).isInstanceOf(WorkshopEmployee::class.java)
        assertThat(result).isEqualTo(access)
    }

    @Test
    fun `should return owner access with all authorities possible that have been set`() {
        // given
        val springCurrentAuthenticationProvider = mockk<SpringCurrentAuthenticationProvider>()
        val currentUserDetails = mockk<UserDetails>()
        every { currentUserDetails.username } returns "testUsername"
        val currentAuth = mockk<Authentication>()
        every { currentAuth.principal } returns currentUserDetails
        every { currentAuth.details } returns null
        every { currentAuth.name } returns "UsernamePasswordAuthenticationToken"
        every { springCurrentAuthenticationProvider.getAuthentication() } returns currentAuth
        val springSecurityContextProvider = spyk<SpringSecurityContextProvider>()
        val securityContext = mockk<SecurityContext>()
        every { securityContext.authentication } returns currentAuth
        every { securityContext.authentication = any() } answers { }
        every { springSecurityContextProvider.getSecurityContext() } returns securityContext
        val testee = SpringApplicationAccessesSetter(
            springCurrentAuthenticationProvider,
            springSecurityContextProvider
        )
        val access = WorkshopOwner(
            accountId = UUID.randomUUID(),
            workshopId = UUID.randomUUID()
        )

        // when
        val result = assertDoesNotThrow { testee.set(access) }

        // then
        assertThat(result).isInstanceOf(WorkshopOwner::class.java)
        assertThat(result).isEqualTo(access)
        assertThat(result.authorities).containsAll(Authority.values().toSet())
    }
}
