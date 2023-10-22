package pl.dashclever.tests.unit.spring.security

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import pl.dashclever.accountresources.account.readmodel.AccessesReader
import pl.dashclever.accountresources.account.readmodel.AccessesReader.EmployeeAccessDto
import pl.dashclever.accountresources.account.readmodel.OwnerAccessDto
import pl.dashclever.accountresources.employee.Workplace
import pl.dashclever.accountresources.employee.Workplace.LABOUR
import pl.dashclever.commons.security.Access
import pl.dashclever.commons.security.Access.WorkshopEmployeeAccess
import pl.dashclever.commons.security.Access.WorkshopOwnerAccess
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.spring.security.SpringApplicationAccessesSetter
import pl.dashclever.spring.security.SpringCurrentAuthenticationProvider
import pl.dashclever.spring.security.SpringSecurityContextProvider
import java.util.UUID

// TODO(This tests defiantly takes to much to set up. Given. The SpringApplicationAccessesSetter does to much. Probably querying for access and preparing the proper Access should be externalized)
internal class SpringApplicationAccessSetter {

    @Test
    fun `should return owner access that have been set`() {
        // given
        val accessReader = mockk<AccessesReader>()
        val currentAccessProvider = mockk<CurrentAccessProvider>()
        val loggedInAccount = object : Access {
            override val accountId: UUID = UUID.randomUUID()
        }
        every { currentAccessProvider.currentAccess() } returns loggedInAccount
        val springCurrentAuthenticationProvider = mockk<SpringCurrentAuthenticationProvider>()
        val currentAuth = mockk<Authentication>()
        every { springCurrentAuthenticationProvider.getAuthentication() } returns currentAuth
        val springSecurityContextProvider = spyk<SpringSecurityContextProvider>()
        val securityContext = mockk<SecurityContext>()
        every { securityContext.authentication } returns currentAuth
        every { securityContext.authentication = any() } answers { }
        every { springSecurityContextProvider.getSecurityContext() } returns securityContext
        val testee = SpringApplicationAccessesSetter(
            accessReader,
            currentAccessProvider,
            springSecurityContextProvider,
            springCurrentAuthenticationProvider
        )
        val workshopId = UUID.randomUUID()
        val workshopAccess = object : OwnerAccessDto {
            override val workshopId: UUID = workshopId
            override val workshopName: String = "testWorkshop"
        }
        every { accessReader.findWorkshopOwnerAccesses(loggedInAccount.accountId) } returns setOf(workshopAccess)

        // when
        val result = testee.setOwnerAccess(workshopId)

        // then
        assertThat(result).isInstanceOf(WorkshopOwnerAccess::class.java)
        assertThat(result).satisfies(
            {
                assertThat(it.workshopId).isEqualTo(workshopId)
                assertThat(it.accountId).isEqualTo(loggedInAccount.accountId)
            }
        )
    }

    @Test
    fun `should return employee access that have been set`() {
        // given
        val accessReader = mockk<AccessesReader>()
        val currentAccessProvider = mockk<CurrentAccessProvider>()
        val loggedInAccount = object : Access {
            override val accountId: UUID = UUID.randomUUID()
        }
        every { currentAccessProvider.currentAccess() } returns loggedInAccount
        val springCurrentAuthenticationProvider = mockk<SpringCurrentAuthenticationProvider>()
        val currentAuth = mockk<Authentication>()
        every { springCurrentAuthenticationProvider.getAuthentication() } returns currentAuth
        val springSecurityContextProvider = spyk<SpringSecurityContextProvider>()
        val securityContext = mockk<SecurityContext>()
        every { securityContext.authentication } returns currentAuth
        every { securityContext.authentication = any() } answers { }
        every { springSecurityContextProvider.getSecurityContext() } returns securityContext
        val testee = SpringApplicationAccessesSetter(
            accessReader,
            currentAccessProvider,
            springSecurityContextProvider,
            springCurrentAuthenticationProvider
        )
        val employeeId = UUID.randomUUID()
        val employeeAccess = object : EmployeeAccessDto {
            override val workshopId: UUID = UUID.randomUUID()
            override val workshopName: String = "testWorkshop"
            override val employeeId: UUID = employeeId
            override val employeeFirstName: String = "testEmployee"
            override val employeeWorkplace: Workplace = LABOUR
        }
        every { accessReader.findEmployeeAccesses(loggedInAccount.accountId) } returns setOf(employeeAccess)

        // when
        val result = testee.setEmployeeAccess(employeeId)

        // then
        assertThat(result).isInstanceOf(WorkshopEmployeeAccess::class.java)
        assertThat(result).satisfies(
            {
                assertThat(it.employeeId).isEqualTo(employeeId)
                assertThat(it.accountId).isEqualTo(loggedInAccount.accountId)
            }
        )
    }
}
