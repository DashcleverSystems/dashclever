package pl.dashclever.tests.unit.spring.security

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.security.core.userdetails.UserDetails
import pl.dashclever.accountresources.account.readmodel.AccountDto
import pl.dashclever.accountresources.account.readmodel.AccountReader
import pl.dashclever.accountresources.account.readmodel.CredentialsDto
import pl.dashclever.accountresources.account.readmodel.CredentialsReader
import pl.dashclever.spring.security.EntryUserDetailsService
import pl.dashclever.spring.security.IdUserDetails
import java.util.Optional
import java.util.UUID

@Suppress("MaxLineLength")
internal class EntryLoginTests {

    @Test
    fun `after logging in in there should be EntryUserDetails without any authorities - no access anywhere - User will decide later which workshop wants to use later on`() {
        // given
        val credentialsReader: CredentialsReader = mockk()
        val credentials = object : CredentialsDto {
            override val accId = UUID.randomUUID()
            override val username = "username"
            override val password = "BCryptPassHash"
        }
        every { credentialsReader.findByUsername("username") }.returns(Optional.of(credentials))
        val testee = EntryUserDetailsService(credentialsReader)

        // when
        val result: UserDetails = testee.loadUserByUsername("username")

        // then
        assertThat(result.username).isEqualTo("username")
        assertThat(result.authorities).isEmpty()
    }

    @Test
    fun `after logging in UserDetails are identifiable by id`() {
        // given
        val credentialsReader: CredentialsReader = mockk()
        val credentials = object : CredentialsDto {
            override val accId = UUID.randomUUID()
            override val username = "username"
            override val password = "BCryptPassHash"
        }
        every { credentialsReader.findByUsername("username") }.returns(Optional.of(credentials))
        val testee = EntryUserDetailsService(credentialsReader)

        // when
        val result: UserDetails = testee.loadUserByUsername("username")

        // then
        assertThat(result).isInstanceOf(IdUserDetails::class.java)
    }
}
