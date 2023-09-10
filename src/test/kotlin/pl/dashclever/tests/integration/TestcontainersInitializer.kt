package pl.dashclever.tests.integration

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.lifecycle.Startables.deepStart
import java.lang.Thread.sleep

internal class TestcontainersInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

	private companion object {

		@JvmStatic
		val POSTGRES = PostgreSQLContainer("postgres:15")

		init {
			deepStart(POSTGRES).join()
		}
	}

	override fun initialize(applicationContext: ConfigurableApplicationContext) {
		sleep(1500)
		TestPropertyValues.of(
			"spring.liquibase.url=${POSTGRES.jdbcUrl}",
			"spring.liquibase.user=${POSTGRES.username}",
			"spring.liquibase.password=${POSTGRES.password}",
			"spring.datasource.url=${POSTGRES.jdbcUrl}",
			"spring.datasource.username=${POSTGRES.username}",
			"spring.datasource.password=${POSTGRES.password}"
		).applyTo(applicationContext.environment)
	}
}
