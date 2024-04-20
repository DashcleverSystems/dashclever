package pl.dashclever.tests.integration.repairmanagment.estimatecatalogue

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.data.auditing.AuditingHandler
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import pl.dashclever.commons.security.Access.WithAuthorities.Authority
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository.EstimateSpecifications
import pl.dashclever.repairmanagment.estimatecatalogue.infrastrucutre.repository.EstimateWorkshopSecuredRepository
import pl.dashclever.repairmanagment.estimatecatalogue.infrastrucutre.repository.WorkshopEstimate
import pl.dashclever.tests.integration.DefaultTestContextConfiguration
import pl.dashclever.tests.integration.spring.TestAccess
import pl.dashclever.tests.integration.spring.TestAccessSetter
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@SpringBootTest
@ExtendWith(MockitoExtension::class)
@Transactional
@DefaultTestContextConfiguration
internal class EstimateWorkshopSecuredReadRepositoryTest(
    @Autowired private val testee: EstimateWorkshopSecuredRepository,
    @Autowired private val estimateWorkshopSecurityRecordTestReadRepository: EstimateWorkshopSecurityRecordTestReadRepository,
    @Autowired @SpyBean
    private val auditingHandler: AuditingHandler,
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
        val estimate = EstimateBuilder { this.estimateName = "24/2023dk" }

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
        testee.save(EstimateBuilder { this.estimateName = "24/2023dk" })

        val anotherAccess = TestAccess(
            accountId = UUID.randomUUID(),
            authorities = Authority.values().toSet(),
            workshopId = UUID.randomUUID()
        )
        testAccessSetter.setAccess(anotherAccess)
        testee.save(EstimateBuilder { this.estimateName = "25/2023dk" })

        // when
        val result = testee.findAll(PageRequest.of(0, 10))

        // then
        val anotherWorkshopestimateNames = this.estimateWorkshopSecurityRecordTestReadRepository.findAllByWorkshopId(anotherAccess.workshopId)
            .map { it.estimateId }
        assertThat(result).allSatisfy { estimate ->
            assertThat(anotherWorkshopestimateNames).contains(estimate.id)
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
        val estimate = EstimateBuilder { this.estimateName = "24/2023dk" }
        testee.save(estimate)

        // when & then
        assertDoesNotThrow { testee.deleteById(estimate.id) }
    }

    @Nested
    inner class FilteringTests {

        @Test
        fun `should filter estimates belonging to currently authenticated access`() {
            // given
            val access = TestAccess(
                accountId = UUID.randomUUID(),
                authorities = Authority.values().toSet(),
                workshopId = UUID.randomUUID()
            )
            testAccessSetter.setAccess(access)
            testee.save(EstimateBuilder { this.estimateName = "24/2023dk" })

            val anotherAccess = TestAccess(
                accountId = UUID.randomUUID(),
                authorities = Authority.values().toSet(),
                workshopId = UUID.randomUUID()
            )
            testAccessSetter.setAccess(anotherAccess)
            testee.save(EstimateBuilder { this.estimateName = "25/2023dk" })

            // when
            val result = testee.findAll(
                EstimateSpecifications.estimateName("25/2023dk"),
                PageRequest.of(0, 10)
            )

            // then
            val allWorkshopEstimates =
                this@EstimateWorkshopSecuredReadRepositoryTest.estimateWorkshopSecurityRecordTestReadRepository.findAllByWorkshopId(anotherAccess.workshopId)
                    .map { it.estimateId }
            assertThat(result).allSatisfy { estimate ->
                assertThat(allWorkshopEstimates).contains(estimate.id)
                assertThat(estimate.name).isEqualTo("25/2023dk")
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
            testee.save(EstimateBuilder { this.estimateName = "23/2023dk" })

            auditingHandler.setDateTimeProvider { Optional.of(wednesday) }
            testee.save(EstimateBuilder { this.estimateName = "24/2023dk" })

            auditingHandler.setDateTimeProvider { Optional.of(thursday) }
            testee.save(EstimateBuilder { this.estimateName = "25/2023dk" })

            auditingHandler.setDateTimeProvider { Optional.of(friday) }
            testee.save(EstimateBuilder { this.estimateName = "26/2023dk" })

            // when
            val result = testee.findAll(
                EstimateSpecifications.createdOnAfter(thursday.minusHours(2)),
                PageRequest.of(0, 10)
            )

            // then
            assertThat(result).satisfiesExactlyInAnyOrder(
                {
                    assertThat(it.name).isEqualTo("25/2023dk")
                },
                {
                    assertThat(it.name).isEqualTo("26/2023dk")
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
            val estimate = EstimateBuilder { this.estimateName = "24/2023dk" }
            testee.save(estimate)

            // when & then
            assertThatCode { testee.deleteById(estimate.id) }.doesNotThrowAnyException()
        }

        @Test
        fun `should find estimates with given customer name`() {
            // given
            val access = TestAccess(
                accountId = UUID.randomUUID(),
                authorities = Authority.values().toSet(),
                workshopId = UUID.randomUUID()
            )
            testAccessSetter.setAccess(access)

            testee.save(
                EstimateBuilder {
                    this.estimateName = "23/2023dk"
                    this.customerName = "Damian Kapłon"
                }
            )

            testee.save(
                EstimateBuilder {
                    this.estimateName = "24/2023dk"
                    this.customerName = "Jane Doe"
                }
            )

            // when
            val result = testee.findAll(
                EstimateSpecifications.customerName("Jane Doe"),
                PageRequest.of(0, 10)
            )

            // then
            assertThat(result).singleElement().satisfies(
                {
                    assertThat(it.customerName).isEqualTo("Jane Doe")
                }
            )
        }

        @Test
        fun `should find estimates with given vehicle brand`() {
            // given
            val access = TestAccess(
                accountId = UUID.randomUUID(),
                authorities = Authority.values().toSet(),
                workshopId = UUID.randomUUID()
            )
            testAccessSetter.setAccess(access)

            testee.save(
                EstimateBuilder {
                    this.estimateName = "23/2023dk"
                    this.vehicleInfo = VehicleInfoBuilder { this.brand = "VOLVO" }
                }
            )

            testee.save(
                EstimateBuilder {
                    this.estimateName = "24/2023dk"
                    this.vehicleInfo = VehicleInfoBuilder { this.brand = "BMW" }
                }
            )

            // when

            val result = testee.findAll(
                EstimateSpecifications.vehicleBrand("BMW"),
                PageRequest.of(0, 10)
            )

            // then
            assertThat(result).singleElement().satisfies(
                {
                    assertThat(it.vehicleInfo.brand).isEqualTo("BMW")
                }
            )
        }

        @Test
        fun `should find only estimates with given registration`() {
            // given
            val access = TestAccess(
                accountId = UUID.randomUUID(),
                authorities = Authority.values().toSet(),
                workshopId = UUID.randomUUID()
            )
            testAccessSetter.setAccess(access)

            testee.save(
                EstimateBuilder {
                    this.estimateName = "23/2023dk"
                    this.vehicleInfo = VehicleInfoBuilder { this.registration = "YYYY11XX2" }
                }
            )

            testee.save(
                EstimateBuilder {
                    this.estimateName = "24/2023dk"
                    this.vehicleInfo = VehicleInfoBuilder { this.registration = "XXX11YY2" }
                }
            )

            // when
            val result = testee.findAll(
                EstimateSpecifications.vehicleRegistration("XXX11YY2"),
                PageRequest.of(0, 10)
            )

            // then
            assertThat(result).singleElement().satisfies(
                {
                    assertThat(it.vehicleInfo.registration).isEqualTo("XXX11YY2")
                }
            )
        }
    }
}
