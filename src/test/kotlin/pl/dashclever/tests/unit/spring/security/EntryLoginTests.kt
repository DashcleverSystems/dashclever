package pl.dashclever.tests.unit.spring.security

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.security.core.userdetails.UserDetails
import pl.dashclever.accountresources.account.readmodel.AccountDto
import pl.dashclever.accountresources.account.readmodel.AccountReader
import pl.dashclever.spring.security.EntryUserDetailsService
import pl.dashclever.spring.security.IdUserDetails
import java.util.Optional
import java.util.UUID

@Suppress("MaxLineLength")
internal class EntryLoginTests {

    @Test
    fun `after logging in in there should be EntryUserDetails without any authorities - no access anywhere - User will decide later which workshop wants to use later on`() {
        // given
        val accountReader: AccountReader = mockk()
        val account = AccountDto(
            id = UUID.randomUUID(),
            username = "username",
            passwordHash = "BCryptPassHash",
            email = "email@email.com"
        )
        every { accountReader.findByUsername("username") }.returns(Optional.of(account))
        val testee = EntryUserDetailsService(accountReader)

        // when
        val result: UserDetails = testee.loadUserByUsername("username")

        // then
        assertThat(result.username).isEqualTo("username")
        assertThat(result.authorities).isEmpty()
    }

    @Test
    fun `after logging in UserDetails are identifiable by id`() {
        // given
        val accountReader: AccountReader = mockk()
        val account = AccountDto(
            id = UUID.randomUUID(),
            username = "username",
            passwordHash = "BCryptPassHash",
            email = "email@email.com"
        )
        every { accountReader.findByUsername("username") }.returns(Optional.of(account))
        val testee = EntryUserDetailsService(accountReader)

        // when
        val result: UserDetails = testee.loadUserByUsername("username")

        // then
        assertThat(result).isInstanceOf(IdUserDetails::class.java)
    }
}
