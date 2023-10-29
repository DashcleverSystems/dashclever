package pl.dashclever.tests.integration.repairmanagment.employee

import io.restassured.RestAssured
import io.restassured.filter.log.LogDetail.ALL
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import pl.dashclever.repairmanagment.plannig.model.PlanCreating
import pl.dashclever.repairmanagment.plannig.model.PlanRepository
import pl.dashclever.repairmanagment.plannig.readmodel.EmployeeOccupationDto
import pl.dashclever.tests.integration.TestcontainersInitializer
import pl.dashclever.tests.integration.repairmanagment.estimatecatalogue.EstimateTestsRepository
import pl.dashclever.tests.integration.repairmanagment.`new estimate`
import java.time.LocalDate
import java.util.UUID

@Disabled("Not yet decided how to test API in regards to Security")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class EmployeeOccupationDtoReadTests(
    @LocalServerPort private val port: Int,
    @Autowired private val estimateRepository: pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository,
    @Autowired private val estimateTestsRepository: EstimateTestsRepository,
    @Autowired private val planCreating: PlanCreating,
    @Autowired private val planRepository: PlanRepository
) {

    @BeforeEach
    fun set() {
        RestAssured.port = port
    }

    @AfterEach
    fun clean() =
        estimateTestsRepository.deleteAll()

    @Test
    fun `employee occupation read test`() {
        // given
        val estimate = `new estimate`("testEstimateUniqueUserId")
        estimateRepository.save(estimate)
        val planId = planCreating.create(estimate.id.toString())
        val plan = planRepository.findById(planId)!!
        val job = estimate.jobs.first()
        val employeeId = UUID.randomUUID().toString()
        plan.assign(job.id!!, employeeId, LocalDate.of(2023, 8, 8))
        planRepository.save(plan)

        // when
        Given {
            param("at", "2023-08-08")
            log().ifValidationFails(ALL)
        } When {
            get("api/employee/$employeeId/occupation")
        } Then {
            log().ifValidationFails()
            statusCode(200)
        } Extract {
            body().`as`(EmployeeOccupationDto::class.java)
        }
    }
}
