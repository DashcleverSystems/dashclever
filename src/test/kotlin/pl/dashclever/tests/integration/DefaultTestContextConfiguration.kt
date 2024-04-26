package pl.dashclever.tests.integration

import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(
    initializers = [
        TestcontainersInitializer::class
    ]
)
internal annotation class DefaultTestContextConfiguration
