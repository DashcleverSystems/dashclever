package pl.dashclever.tests.unit.spring.security

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.dashclever.accountresources.account.readmodel.AccessesReader
import pl.dashclever.accountresources.account.readmodel.AccessesReader.EmployeeAccessDto
import pl.dashclever.accountresources.account.readmodel.OwnerAccessDto
import pl.dashclever.accountresources.employee.Workplace
import pl.dashclever.accountresources.employee.Workplace.LABOUR
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.commons.security.WithAccountId
import pl.dashclever.commons.security.WithAuthorities.Authority
import pl.dashclever.commons.security.WithAuthorities.Authority.REPAIR_PROCESS
import pl.dashclever.commons.security.WorkshopEmployee
import pl.dashclever.commons.security.WorkshopOwner
import pl.dashclever.spring.security.SpringApplicationAccessesSetter
import pl.dashclever.spring.security.SpringSecurityApplicationFacade
import java.util.*

internal class SpringSecurityApplicationFacade {

    @Test
    fun `should return just set employee access with employee authorities`() {
        // given
        val accessReader = mockk<AccessesReader>()
        val currentAccessProvider = mockk<CurrentAccessProvider>()
        val loggedInAccount = object : WithAccountId {
            override val accountId: UUID = UUID.randomUUID()
        }
        every { currentAccessProvider.currentAccountId() } returns loggedInAccount
        val employeeId = UUID.randomUUID()
        val employeeAccessDto = object : EmployeeAccessDto {
            override val workshopId: UUID = UUID.randomUUID()
            override val workshopName: String = "testWorkshop"
            override val employeeId: UUID = employeeId
            override val employeeFirstName: String = "testEmployee"
            override val employeeWorkplace: Workplace = LABOUR
        }
        every { accessReader.findEmployeeAccesses(loggedInAccount.accountId) } returns setOf(employeeAccessDto)
        val springApplicationAccessesSetter = mockk<SpringApplicationAccessesSetter>()
        every { springApplicationAccessesSetter.set(any<WorkshopEmployee>()) } returnsArgument 0

        val testee = SpringSecurityApplicationFacade(
            accessReader,
            currentAccessProvider,
            springApplicationAccessesSetter
        )

        // when
        val result = testee.setEmployeeAccess(employeeId)

        // then
        assertThat(result).satisfies({
            assertThat(it.authorities).isEqualTo(setOf(REPAIR_PROCESS))
            assertThat(it.workshopId).isEqualTo(employeeAccessDto.workshopId)
            assertThat(it.employeeId).isEqualTo(employeeAccessDto.employeeId)
        })
    }

    @Test
    fun `should return just set owner access with all authorities possible`() {
        // given
        val accessReader = mockk<AccessesReader>()
        val currentAccessProvider = mockk<CurrentAccessProvider>()
        val loggedInAccount = object : WithAccountId {
            override val accountId: UUID = UUID.randomUUID()
        }
        every { currentAccessProvider.currentAccountId() } returns loggedInAccount
        val workshopId = UUID.randomUUID()
        val ownerAccessDto = object : OwnerAccessDto {
            override val workshopId: UUID = workshopId
            override val workshopName: String = "testWorkshop"
        }
        every { accessReader.findWorkshopOwnerAccesses(loggedInAccount.accountId) } returns setOf(ownerAccessDto)
        val springApplicationAccessesSetter = mockk<SpringApplicationAccessesSetter>()
        every { springApplicationAccessesSetter.set(any<WorkshopOwner>()) } returnsArgument 0

        val testee = SpringSecurityApplicationFacade(
            accessReader,
            currentAccessProvider,
            springApplicationAccessesSetter
        )

        // when
        val result = testee.setOwnerAccess(workshopId)

        // then
        assertThat(result).satisfies({
            assertThat(it.authorities).isEqualTo(Authority.values().toSet())
            assertThat(it.workshopId).isEqualTo(ownerAccessDto.workshopId)
        })
    }
}
