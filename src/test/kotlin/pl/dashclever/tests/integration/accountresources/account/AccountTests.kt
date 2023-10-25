package pl.dashclever.tests.integration.accountresources.account

import io.restassured.RestAssured
import io.restassured.filter.log.LogDetail.ALL
import io.restassured.http.ContentType.JSON
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
import pl.dashclever.tests.integration.TestcontainersInitializer

@Disabled("Not yet decided how to test API in regards to Security")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class AccountTests(
    @Autowired private val accountCleaner: AccountCleaner,
    @LocalServerPort private val port: Int
) {

    @BeforeEach
    fun set() {
        RestAssured.port = port
    }

    @AfterEach
    fun clean() {
        accountCleaner.deleteAll()
    }

    @Test
    fun `should create account`() {
        Given {
            contentType(JSON)
            body(
                """
                    {
                        "username": "testUsername",
                        "password": "testPassword",
                        "email": "test@email.com"
                    }
                """.trimIndent()
            )
            log().ifValidationFails()
        } When {
            post("api/account")
        } Then {
            log().ifValidationFails(ALL)
            assertThat()
            statusCode(202)
        }
    }
}
