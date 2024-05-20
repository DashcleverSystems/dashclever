package pl.dashclever.tests.integration.accountresources.account.readmodel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.dashclever.accountresources.account.model.Account
import pl.dashclever.accountresources.account.model.AccountRepository
import pl.dashclever.accountresources.account.readmodel.CredentialsReader
import pl.dashclever.tests.integration.DefaultTestContextConfiguration

@SpringBootTest
@DefaultTestContextConfiguration
internal class CredentialsReaderTest(
    @Autowired private val accountRepository: AccountRepository,
    @Autowired private val credentialsReader: CredentialsReader
) {

    @Test
    fun `should return accounts credentials`() {
        // given
        val account = Account(
            username = "username",
            passwordHash = "passwordHash",
            email = "email@email.com"
        )
        accountRepository.save(account)

        // when
        val result = credentialsReader.findByUsername("username")

        // then
        assertThat(result).hasValueSatisfying {
            assertThat(it.accId).isEqualTo(account.id)
            assertThat(it.username).isEqualTo("username")
            assertThat(it.password).isEqualTo("passwordHash")
        }
    }
}
