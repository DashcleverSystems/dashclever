package pl.dashclever.tests.integration.repairmanagment.estimatecatalogue

import io.restassured.RestAssured
import io.restassured.filter.log.LogDetail.ALL
import io.restassured.http.ContentType.JSON
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import pl.dashclever.tests.integration.TestcontainersInitializer
import pl.dashclever.tests.integration.repairmanagment.`new estimate`

@Disabled("Not yet decided how to test API in regards to Security")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class EstimateCatalogueTests(
    @LocalServerPort private val port: Int,
    @Autowired private val estimateRepository: pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository,
    @Autowired private val estimateTestsRepository: EstimateTestsRepository
) {

    @BeforeEach
    fun set() {
        RestAssured.port = port
        estimateTestsRepository.deleteAll()
    }

    @Test
    fun `should create estimate`() {
        Given {
            contentType(JSON)
            body(
                """
            {
                "estimateId": "22/2022wk",
                "vehicleInfo": {
                  "registration": "ZKO8GG2",
                  "brand": "PEUGEOT",
                  "model": "307"
                },
                "paintInfo": {
                  "baseColorWithCode": "blue",
                  "varnishingPaintInfo": "2 layers pearl"
                },
                "jobs": [
                  {
                    "name": "mount/unmount bumpers",
                    "manMinutes": 240,
                    "worth": {
                      "denomination": 240,
                      "currency": "PLN"
                    },
                    "jobType": "LABOUR"
                  }
                ]
            }
                """.trimIndent()
            )
            log().ifValidationFails()
        } When {
            post("api/estimatecatalogue")
        } Then {
            log().ifValidationFails(ALL)
            assertThat()
            statusCode(201)
        }
    }

    @Test
    fun `should filter estimates by estimateId`() {
        // given
        val estimateId = "24/2022wk"
        val estimates = arrayOf(`new estimate`(estimateId))
        estimateTestsRepository.saveAll(estimates.toSet())

        // when
        val response =
            Given {
                queryParam("estimateId", estimateId)
                log().ifValidationFails(ALL)
            } When {
                get("api/estimatecatalogue")
            } Then {
                log().ifValidationFails(ALL)
                statusCode(200)
            } Extract {
                response().body().`as`(Array<pl.dashclever.repairmanagment.estimatecatalogue.Estimate>::class.java)
            }

        // then
        assertThat(estimates).usingRecursiveComparison().isEqualTo(response)
    }

    @Test
    fun `should return all estimates`() {
        // given
        val estimates = arrayOf(`new estimate`("1/2022wk"), `new estimate`("2/2022kw"))
        estimateTestsRepository.saveAll(estimates.toSet())

        // when
        val response =
            Given {
                log().ifValidationFails(ALL)
            } When {
                get("api/estimatecatalogue")
            } Then {
                log().ifValidationFails(ALL)
                statusCode(200)
            } Extract {
                response().body().`as`(Array<pl.dashclever.repairmanagment.estimatecatalogue.Estimate>::class.java)
            }

        // then
        assertThat(estimates).usingRecursiveComparison().isEqualTo(response)
    }

    @Test
    fun `should delete estimate`() {
        // given
        val estimateId = "25/2022wk"
        val estimate = `new estimate`(estimateId)
        estimateRepository.save(estimate)

        // when
        Given {
            queryParam("estimateId", estimate.id)
            log().ifValidationFails(ALL)
        } When {
            delete("api/estimatecatalogue")
        } Then {
            log().ifValidationFails(ALL)
            statusCode(204)
        }
    }

    @Test
    fun `should fail safe delete estimate`() {
        // given
        val estimateId = "26/2022wk"
        val estimate = `new estimate`(estimateId)
        estimateRepository.save(estimate)
        val estimateUid = estimate.id
        estimateRepository.deleteById(estimate.id)

        // when
        Given {
            queryParam("estimateId", estimateUid)
            log().ifValidationFails(ALL)
        } When {
            delete("api/estimatecatalogue")
        } Then {
            log().ifValidationFails(ALL)
            statusCode(204)
        }
    }
}
