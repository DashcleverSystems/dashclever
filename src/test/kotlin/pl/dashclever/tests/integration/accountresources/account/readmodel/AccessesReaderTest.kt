package pl.dashclever.tests.integration.accountresources.account.readmodel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.accountresources.account.model.Account
import pl.dashclever.accountresources.account.model.AccountRepository
import pl.dashclever.accountresources.account.readmodel.AccessesReader
import pl.dashclever.tests.integration.TestcontainersInitializer

@SpringBootTest
@Transactional
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class AccessesReaderTest(
    @Autowired private val accountRepository: AccountRepository,
    @Autowired private val accessesReader: AccessesReader
) {

    @Test
    fun `should return owner accesses`() {
        // given
        val account = Account(
            username = "username",
            passwordHash = "passwordHash",
            email = "email@email.com"
        )
        account.createWorkshop("workshopName")
        accountRepository.save(account)

        // when
        val result = accessesReader.findWorkshopOwnerAccesses(account.id)

        // then
        assertThat(result).singleElement().satisfies(
            {
                assertThat(it.workshopName).isEqualTo("workshopName")
            }
        )
    }
}
