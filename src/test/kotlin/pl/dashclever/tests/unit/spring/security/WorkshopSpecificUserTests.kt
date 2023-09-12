// package pl.dashclever.tests.unit.spring.security
//
// import io.mockk.every
// import io.mockk.mockk
// import org.assertj.core.api.Assertions.assertThat
// import org.junit.jupiter.api.Test
// import org.springframework.security.core.Authentication
// import pl.dashclever.accountresources.account.readmodel.AccessDto
// import pl.dashclever.accountresources.account.readmodel.pl.dashclever.accountresources.account.readmodel.AccessesReader
// import pl.dashclever.accountresources.account.readmodel.Authority.MANAGE_STAFF
// import pl.dashclever.spring.security.IdUserDetails
// import pl.dashclever.spring.security.WorkshopUserDetailsService
// import java.util.UUID
//
// internal class WorkshopSpecificUserTests {
//
//    @Test
//    fun `When users are logged in they decided which workshop they want to be authorise to`() {
//        // given
//        val accessesReader: pl.dashclever.accountresources.account.readmodel.AccessesReader = mockk()
//        val workshopUserDetailsService = WorkshopUserDetailsService(accessesReader)
//        val currentAuthentication: Authentication = mockk()
//        val currentAccountId = UUID.randomUUID()
//        val currentUser: IdUserDetails = mockk()
//        val requestedAccessesWorkshopId = UUID.randomUUID()
//        every { currentAuthentication.principal } returns currentUser
//        every { currentUser.id } returns currentAccountId
//        every { currentUser.username } returns "username"
//        every { accessesReader.findAccountAccesses(currentAccountId) } returns setOf(
//            AccessDto(
//                workshopId = requestedAccessesWorkshopId,
//                employeeId = null,
//                authorities = setOf(MANAGE_STAFF),
//            )
//        )
//
//        // when
//        val result = workshopUserDetailsService.workshopSpecificUserOfAuthentication(requestedAccessesWorkshopId, currentAuthentication)
//
//        // then
//        assertThat(result.workshopId).isEqualTo(requestedAccessesWorkshopId)
//        assertThat(result).isInstanceOf(IdUserDetails::class.java)
//    }
// }
