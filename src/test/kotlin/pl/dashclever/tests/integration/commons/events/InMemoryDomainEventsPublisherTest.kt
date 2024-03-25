package pl.dashclever.tests.integration.commons.events

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents
import org.springframework.transaction.support.TransactionTemplate
import pl.dashclever.commons.events.DomainEvent
import pl.dashclever.commons.events.DomainEvents
import pl.dashclever.tests.integration.TestcontainersInitializer
import java.util.*


@SpringBootTest(webEnvironment = RANDOM_PORT)
@RecordApplicationEvents
@ContextConfiguration(initializers = [TestcontainersInitializer::class])
internal class InMemoryDomainEventsPublisherTest(
) {

    @Autowired
    lateinit var domainEvents: DomainEvents
    @Autowired
    lateinit var transactionTemplate: TransactionTemplate
    @Autowired
    lateinit var applicationEvents: ApplicationEvents

    @Test
    fun `should publish event after commit`() {
        // given
        val testDomainEvent = TestDomainEvent()

        // when
        transactionTemplate.executeWithoutResult {
            domainEvents.publish(testDomainEvent)
        }

        // then
        assertThat(applicationEvents.stream().toList()).hasSize(1)
    }

    @Test
    fun `should not publish an event when transaction failed`() {
        // given
        val testDomainEvent = TestDomainEvent()

        // when
        transactionTemplate.executeWithoutResult {
            domainEvents.publish(testDomainEvent)
        }

        // then
        assertThat(applicationEvents.stream().toList()).isEmpty()
    }
}

private data class TestDomainEvent(
    override val id: UUID = UUID.randomUUID()
) : DomainEvent
