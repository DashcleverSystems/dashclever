package pl.dashclever.tests.integration.repairmanagment.repairing.infrastructure.persistance

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
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
    @Autowired private val repairWorkshopSecuredRepository: EntitySecurityRecordRepository<Repair, UUID, RepairWorkshop>
) {

    private val testAccess = TestAccess(
        accountId = UUID.randomUUID(),
        authorities = emptySet(),
        workshopId = UUID.randomUUID()
    )
    private val testAccessSetter = TestAccessSetter()

    @BeforeEach
    fun setUp() {
        testAccessSetter.setAccess(testAccess)
    }

    @AfterEach
    fun tearDown() {
        testAccessSetter.setAccess(null)
    }

    @Test
    fun `should create a security record`() {
        // given
        val repair = Repair(planId = UUID.randomUUID())

        // when
        repairRepository.save(repair)

        // then
        assertThat(repairWorkshopSecuredRepository.doesSecurityRecordExistFor(repair)).isTrue()
    }

    @Test
    fun `should find existing repair of given plans belonging to workshop`() {
        // given
        val planId = UUID.randomUUID()
        repairRepository.save(Repair(planId))
        repairRepository.save(Repair(planId))
        repairRepository.save(Repair(UUID.randomUUID()))

        // when
        val result = repairRepository.anyRunningRepairOfPlanIdIn(setOf(planId))

        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `should not find existing repair of given plans belonging to workshop`() {
        // given
        val planId = UUID.randomUUID()
        val planIdOfNoneRepair = UUID.randomUUID()
        repairRepository.save(Repair(planId))
        repairRepository.save(Repair(planId))
        repairRepository.save(Repair(UUID.randomUUID()))

        // when
        val result = repairRepository.anyRunningRepairOfPlanIdIn(setOf(planIdOfNoneRepair))

        // then
        assertThat(result).isFalse()
    }
}
