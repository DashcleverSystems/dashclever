package pl.dashclever.tests.unit.accountresources.account

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import pl.dashclever.accountresources.account.model.Account
import pl.dashclever.accountresources.employee.Employee
import pl.dashclever.accountresources.employee.Workplace.LABOUR
import pl.dashclever.publishedlanguage.DomainException
import java.util.UUID

internal class AccountTests {

    @Test
    fun `Account can have max 2 workshops`() {
        // given
        val account = Account(
            username = "username",
            passwordHash = "passHash",
            email = "email@email.com"
        )
        account.createWorkshop("workshop1")
        account.createWorkshop("workshop2")

        // when
        val result = assertThrows<DomainException> { account.createWorkshop("workshop3") }
        assertThat(result).hasMessage("Your account can be owner of only two workshops")
    }

    @Test
    fun `Account can have associated with it max 5 employees`() {
        // given
        val account = Account(
            username = "username",
            passwordHash = "passHash",
            email = "email@email.com"
        )
        account.associateWith(Employee("firstName1", "lastName1", UUID.randomUUID(), LABOUR))
        account.associateWith(Employee("firstName2", "lastName2", UUID.randomUUID(), LABOUR))
        account.associateWith(Employee("firstName3", "lastName3", UUID.randomUUID(), LABOUR))
        account.associateWith(Employee("firstName4", "lastName4", UUID.randomUUID(), LABOUR))
        account.associateWith(Employee("firstName5", "lastName5", UUID.randomUUID(), LABOUR))

        // when
        assertThrows<DomainException> { account.associateWith(Employee("firstName6", "lastName6", UUID.randomUUID(), LABOUR)) }
    }
}
