package pl.dashclever.tests.integration.repairmanagment.planning

import io.restassured.RestAssured
import io.restassured.filter.log.LogDetail.ALL
import io.restassured.http.ContentType.JSON
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
import pl.dashclever.repairmanagment.plannig.model.PlanCreating
import pl.dashclever.tests.integration.TestcontainersInitializer
import pl.dashclever.tests.integration.repairmanagment.estimatecatalogue.EstimateTestsRepository
import pl.dashclever.tests.integration.repairmanagment.`new estimate`

@Disabled("Not yet decided how to test API in regards to Security")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class PlanningTests(
    @LocalServerPort private val port: Int,
    @Autowired private val estimateRepository: pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository,
    @Autowired private val estimateTestsRepository: EstimateTestsRepository,
    @Autowired private val planCreating: PlanCreating
) {

    @BeforeEach
    fun set() {
        RestAssured.port = port
        estimateTestsRepository.deleteAll()
    }

    @Test
    fun `should create planing`() {
        // given
        val estimate = `new estimate`("testEstimateUniqueUserId")
        estimateRepository.save(estimate)

        // when
        Given {
            queryParam("estimateId", estimate.id)
            log().ifValidationFails(ALL)
        } When {
            post("api/planning")
        } Then {
            log().ifValidationFails(ALL)
            statusCode(201)
        }
    }

    @Test
    fun `should assign job`() {
        // given
        val estimate = `new estimate`("testEstimateUniqueUserId")
        estimateRepository.save(estimate)
        val planId = planCreating.create(estimate.id.toString())

        // when
        Given {
            contentType(JSON)
            body(
                """
                    {
                      "to": "labourEmployeeId",
                      "at": "2023-01-22"
                    }
                """.trimIndent()
            )
            log().ifValidationFails(ALL)
        } When {
            patch("api/planning/$planId/job/${estimate.jobs.first().id!!}")
        } Then {
            log().ifValidationFails(ALL)
            statusCode(204)
        }
    }
}
