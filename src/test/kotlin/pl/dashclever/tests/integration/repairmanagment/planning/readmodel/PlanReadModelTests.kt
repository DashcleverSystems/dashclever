package pl.dashclever.tests.integration.repairmanagment.planning.readmodel

import io.restassured.RestAssured
import io.restassured.filter.log.LogDetail.ALL
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository
import pl.dashclever.repairmanagment.plannig.model.PlanCreating
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.plannig.readmodel.PlanDto
import pl.dashclever.tests.integration.TestcontainersInitializer
import pl.dashclever.tests.integration.repairmanagment.estimatecatalogue.EstimateTestsRepository
import pl.dashclever.tests.integration.repairmanagment.`new estimate`
import java.time.LocalDate

@Disabled("Not yet decided how to test API in regards to Security")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class PlanReadModelTests(
    @LocalServerPort private val port: Int,
    @Autowired private val estimateRepository: EstimateRepository,
    @Autowired private val estimateTestsRepository: EstimateTestsRepository,
    @Autowired private val planCreating: PlanCreating,
    @Autowired private val planRepository: PlanRepository
) {

    @BeforeEach
    fun set() {
        RestAssured.port = port
        estimateTestsRepository.deleteAll()
    }

    @Test
    fun `should find plan`() {
        // given
        val estimate = `new estimate`("24/2022wk")
        estimateRepository.save(estimate)
        val planningId = planCreating.create(estimate.id.toString())
        val planning = planRepository.findById(planningId).get()
        planning.assign(estimate.jobs.first().id!!, "employeeId", LocalDate.of(2022, 2, 2))
        planRepository.save(planning)

        // when
        Given {
            log().ifValidationFails(ALL)
        } When {
            get("api/planning/${planning.id}")
        } Then {
            log().ifValidationFails(ALL)
            statusCode(200)
        } Extract {
            response().body().`as`(PlanDto::class.java)
        }
    }

    @Test
    fun `should find plan by estimate id`() {
        // given
        val estimate = `new estimate`("24/2022wk")
        estimateRepository.save(estimate)
        planCreating.create(estimate.id.toString())

        // when
        Given {
            param("estimateId", estimate.id)
            log().ifValidationFails(ALL)
        } When {
            get("api/planning")
        } Then {
            log().ifValidationFails(ALL)
            statusCode(200)
        } Extract {
            response().body().`as`(Array<PlanDto>::class.java)
        }
    }

    @Test
    fun `should find plan within date range`() {
        // given
        val estimate = `new estimate`("24/2022wk")
        estimateRepository.save(estimate)
        val planningId = planCreating.create(estimate.id.toString())
        val planning = planRepository.findById(planningId).get()
        planning.assign(estimate.jobs.first().id!!, "employeeId", LocalDate.of(2022, 2, 2))
        planRepository.save(planning)

        // when
        Given {
            param("from", "2022-01-28")
            param("to", "2022-02-22")
            log().ifValidationFails(ALL)
        } When {
            get("api/planning")
        } Then {
            log().ifValidationFails(ALL)
            statusCode(200)
        } Extract {
            response().body().`as`(Array<PlanDto>::class.java)
        }
    }
}
