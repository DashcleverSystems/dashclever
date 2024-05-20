package pl.dashclever.tests.integration.spring.security

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ContextConfiguration
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.commons.security.SystemAccessSetter
import pl.dashclever.commons.security.SystemOnBehalfOfWorkshop
import pl.dashclever.tests.integration.TestcontainersInitializer
import java.util.UUID

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class SystemAuthenticationOnBehalfOfWorkshopTests(
    @Autowired private val systemAccessSetter: SystemAccessSetter,
    @Autowired private val currentAccessProvider: CurrentAccessProvider
) {

    @Test
    fun `should authenticate as system user on behalf of given workshop`() {
        // given
        val workshopId = UUID.randomUUID()

        // when
        systemAccessSetter.set(SystemOnBehalfOfWorkshop(workshopId))

        // then
        val currentAccess = currentAccessProvider.currentWorkshopId()
        assertThat(currentAccess).satisfies(
            {
                assertThat(it.workshopId).isEqualTo(workshopId)
            }
        )
    }
}
