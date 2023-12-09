package pl.dashclever.tests.integration.repairmanagment.estimatecatalogue

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.data.auditing.AuditingHandler
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.commons.security.Access.WithAuthorities.Authority
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateSpecifications
import pl.dashclever.repairmanagment.estimatecatalogue.infrastrucutre.repository.EstimateWorkshopSecuredRepository
import pl.dashclever.repairmanagment.estimatecatalogue.infrastrucutre.repository.WorkshopEstimate
import pl.dashclever.tests.integration.TestcontainersInitializer
import pl.dashclever.tests.integration.spring.TestAccess
import pl.dashclever.tests.integration.spring.TestAccessSetter
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@ExtendWith(MockitoExtension::class)
@Transactional
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class EstimateWorkshopSecuredReadRepositoryTest(
    @Autowired private val testee: EstimateWorkshopSecuredRepository,
    @Autowired private val estimateWorkshopSecurityRecordTestReadRepository: EstimateWorkshopSecurityRecordTestReadRepository,
    @Autowired @SpyBean
    private val auditingHandler: AuditingHandler
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
        val estimate = EstimateBuilder { this.estimateId = "24/2023dk" }

        // when
        testee.save(estimate)

        // then
        val estimateSecurityRecords: Set<WorkshopEstimate> = estimateWorkshopSecurityRecordTestReadRepository.findAllByWorkshopId(access.workshopId)
        assertThat(estimateSecurityRecords).anyMatch { it.estimateId == estimate.id && it.workshopId == access.workshopId }
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
        testee.save(EstimateBuilder { this.estimateId = "24/2023dk" })

        val anotherAccess = TestAccess(
            accountId = UUID.randomUUID(),
            authorities = Authority.values().toSet(),
            workshopId = UUID.randomUUID()
        )
        testAccessSetter.setAccess(anotherAccess)
        testee.save(EstimateBuilder { this.estimateId = "25/2023dk" })

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
        testee.save(EstimateBuilder { this.estimateId = "24/2023dk" })

        val anotherAccess = TestAccess(
            accountId = UUID.randomUUID(),
            authorities = Authority.values().toSet(),
            workshopId = UUID.randomUUID()
        )
        testAccessSetter.setAccess(anotherAccess)
        testee.save(EstimateBuilder { this.estimateId = "25/2023dk" })

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
    fun `should find only one estimate created after given time`() {
        // given
        val friday = LocalDateTime.of(2023, 12, 1, 11, 0)
        val thursday = friday.minusDays(1)
        val wednesday = thursday.minusDays(1)
        val tuesday = wednesday.minusDays(1)

        val access = TestAccess(
            accountId = UUID.randomUUID(),
            authorities = Authority.values().toSet(),
            workshopId = UUID.randomUUID()
        )
        testAccessSetter.setAccess(access)

        auditingHandler.setDateTimeProvider { Optional.of(tuesday) }
        testee.save(EstimateBuilder { this.estimateId = "23/2023dk" })

        auditingHandler.setDateTimeProvider { Optional.of(wednesday) }
        testee.save(EstimateBuilder { this.estimateId = "24/2023dk" })

        auditingHandler.setDateTimeProvider { Optional.of(thursday) }
        testee.save(EstimateBuilder { this.estimateId = "25/2023dk" })

        auditingHandler.setDateTimeProvider { Optional.of(friday) }
        testee.save(EstimateBuilder { this.estimateId = "26/2023dk" })

        // when

        val result = testee.findAll(
            EstimateSpecifications.createdOnAfter(thursday.minusHours(2)),
            PageRequest.of(0, 10)
        )

        // then
        assertThat(result).satisfiesExactlyInAnyOrder(
            {
                assertThat(it.estimateId).isEqualTo("25/2023dk")
            },
            {
                assertThat(it.estimateId).isEqualTo("26/2023dk")
            }
        )
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
        val estimate = EstimateBuilder { this.estimateId = "24/2023dk" }
        testee.save(estimate)

        // when & then
        assertDoesNotThrow { testee.deleteById(estimate.id) }
    }
}
