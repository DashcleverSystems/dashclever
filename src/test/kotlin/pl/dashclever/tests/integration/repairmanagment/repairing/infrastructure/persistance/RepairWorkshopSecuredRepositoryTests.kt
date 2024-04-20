package pl.dashclever.tests.integration.repairmanagment.repairing.infrastructure.persistance

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.commons.security.EntitySecurityRecordRepository
import pl.dashclever.repairmanagment.repairing.application.RepairRepository
import pl.dashclever.repairmanagment.repairing.infrastructure.rest.persistance.RepairWorkshop
import pl.dashclever.repairmanagment.repairing.model.Repair
import pl.dashclever.tests.integration.DefaultTestContextConfiguration
import pl.dashclever.tests.integration.spring.TestAccess
import pl.dashclever.tests.integration.spring.TestAccessSetter
import java.util.UUID

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@DefaultTestContextConfiguration
internal class RepairWorkshopSecuredRepositoryTests(
    @Autowired private val repairRepository: RepairRepository,
    @Autowired private val repairWorkshopSecuredRepository: EntitySecurityRecordRepository<Repair, UUID, RepairWorkshop>,
) {

    private val testAccessSetter = TestAccessSetter()

    @Test
    fun `should create a security record`() {
        // given
        val testAccess = TestAccess(
            accountId = UUID.randomUUID(),
            authorities = emptySet(),
            workshopId = UUID.randomUUID()
        )
        testAccessSetter.setAccess(testAccess)
        val repair = Repair(planId = UUID.randomUUID())

        // when
        repairRepository.save(repair)

        // then
        assertThat(repairWorkshopSecuredRepository.doesSecurityRecordExistFor(repair)).isTrue()
    }
}
