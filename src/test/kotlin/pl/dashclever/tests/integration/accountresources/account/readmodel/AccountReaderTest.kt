package pl.dashclever.tests.integration.accountresources.account.readmodel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import pl.dashclever.accountresources.account.model.Account
import pl.dashclever.accountresources.account.model.AccountRepository
import pl.dashclever.accountresources.account.readmodel.AccountDto
import pl.dashclever.accountresources.account.readmodel.AccountReader
import pl.dashclever.tests.integration.TestcontainersInitializer
import pl.dashclever.tests.integration.accountresources.account.AccountCleaner

@SpringBootTest
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class AccountReaderTest(
    @Autowired private val accountRepository: AccountRepository,
    @Autowired private val accountReader: AccountReader,
    @Autowired private val accountCleaner: AccountCleaner,
) {

    @AfterEach
    fun deleteAccounts() {
        accountCleaner.deleteAll()
    }

    @Test
    fun `should return proper projection dto`() {
        // given
        val account = Account(
            "username",
            "passwordHash",
            "email@email.com",
        )
        accountRepository.save(account)

        // when
        val result = accountReader.findByUsername("username")

        // then
        assertThat(result).hasValueSatisfying {
            assertThat(it).isInstanceOf(AccountDto::class.java)
            assertThat(it.username).isEqualTo("username")
            assertThat(it.email).isEqualTo("email@email.com")
        }
    }
}
