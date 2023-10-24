package pl.dashclever.tests.integration.repairmanagment.estimatecatalogue

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.commons.security.Access.WithAuthorities.Authority
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateSpecifications
import pl.dashclever.repairmanagment.estimatecatalogue.infrastrucutre.repository.EstimateWorkshopSecuredRepository
import pl.dashclever.tests.integration.TestcontainersInitializer
import pl.dashclever.tests.integration.repairmanagment.`new estimate`
import pl.dashclever.tests.integration.spring.TestAccess
import pl.dashclever.tests.integration.spring.TestAccessSetter
import java.util.UUID

@SpringBootTest
@Transactional
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class EstimateWorkshopSecuredReadRepositoryTest(
    @Autowired private val testee: EstimateWorkshopSecuredRepository,
    @Autowired private val estimateWorkshopSecurityRecordTestReadRepository: EstimateWorkshopSecurityRecordTestReadRepository
) {

    private val testAccessSetter = TestAccessSetter()

    @Test
    fun `should create estimate and record informing to which workshop it belongs`() {
        // given
        val access = TestAccess(
            accountId = UUID.randomUUID(),
            authorities = Authority.values().toSet(),
            workshopId = UUID.randomUUID()
        )
        testAccessSetter.setAccess(access)
        val estimate = `new estimate`("24/2023dk")

        // when
        testee.save(estimate)
    }

    @Test
    fun `should return only estimates belonging to currently authenticated access`() {
        // given
        val access = TestAccess(
            accountId = UUID.randomUUID(),
            authorities = Authority.values().toSet(),
            workshopId = UUID.randomUUID()
        )
        testAccessSetter.setAccess(access)
        testee.save(`new estimate`("24/2023dk"))

        val anotherAccess = TestAccess(
            accountId = UUID.randomUUID(),
            authorities = Authority.values().toSet(),
            workshopId = UUID.randomUUID()
        )
        testAccessSetter.setAccess(anotherAccess)
        testee.save(`new estimate`("25/2023dk"))

        // when
        val result = testee.findAll(PageRequest.of(0, 10))

        // then
        val anotherWorkshopEstimateIds = this.estimateWorkshopSecurityRecordTestReadRepository.findAllByWorkshopId(anotherAccess.workshopId)
            .map { it.estimateId }
        assertThat(result).allSatisfy { estimate ->
            assertThat(anotherWorkshopEstimateIds).contains(estimate.id)
        }
    }

    @Test
    fun `should filter estimates belonging to currently authenticated access`() {
        // given
        val access = TestAccess(
            accountId = UUID.randomUUID(),
            authorities = Authority.values().toSet(),
            workshopId = UUID.randomUUID()
        )
        testAccessSetter.setAccess(access)
        testee.save(`new estimate`("24/2023dk"))

        val anotherAccess = TestAccess(
            accountId = UUID.randomUUID(),
            authorities = Authority.values().toSet(),
            workshopId = UUID.randomUUID()
        )
        testAccessSetter.setAccess(anotherAccess)
        testee.save(`new estimate`("25/2023dk"))

        // when
        val result = testee.findAll(
            EstimateSpecifications.estimateId("25/2023dk"),
            PageRequest.of(0, 10)
        )

        // then
        val anotherWorkshopEstimateIds = this.estimateWorkshopSecurityRecordTestReadRepository.findAllByWorkshopId(anotherAccess.workshopId)
            .map { it.estimateId }
        assertThat(result).allSatisfy { estimate ->
            assertThat(anotherWorkshopEstimateIds).contains(estimate.id)
            assertThat(estimate.estimateId).isEqualTo("25/2023dk")
        }
    }

    @Test
    fun `should delete without exception`() {
        // given
        val access = TestAccess(
            accountId = UUID.randomUUID(),
            authorities = Authority.values().toSet(),
            workshopId = UUID.randomUUID()
        )
        testAccessSetter.setAccess(access)
        val estimate = `new estimate`("24/2023dk")
        testee.save(estimate)

        // when & then
        assertDoesNotThrow { testee.deleteById(estimate.id) }
    }
}
