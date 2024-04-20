package pl.dashclever.tests.integration.commons.events

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.context.PayloadApplicationEvent
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents
import org.springframework.transaction.UnexpectedRollbackException
import org.springframework.transaction.support.TransactionTemplate
import pl.dashclever.commons.events.DomainEvent
import pl.dashclever.commons.events.DomainEvents
import pl.dashclever.tests.integration.TestcontainersInitializer
import pl.dashclever.tests.integration.spring.TestInMemorySyncDomainEventsPublisherBeansInitializer
import java.util.*

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RecordApplicationEvents
@ContextConfiguration(initializers = [TestcontainersInitializer::class, TestInMemorySyncDomainEventsPublisherBeansInitializer::class])
internal class InMemoryDomainEventsPublisherTest(
    @Autowired private val domainEvents: DomainEvents,
    @Autowired private val transactionTemplate: TransactionTemplate
) {

    @Test
    fun `should publish an event`(applicationEvents: ApplicationEvents) {
        // given
        val testDomainEvent = TestDomainEvent()

        // when
        transactionTemplate.executeWithoutResult {
            domainEvents.publish(testDomainEvent)
        }

        // then
        assertThat(applicationEvents.stream().toList()).satisfiesOnlyOnce { applicationEvent ->
            assertThat(applicationEvent).isInstanceOf(PayloadApplicationEvent::class.java)
            val payloadApplicationEvent = applicationEvent as PayloadApplicationEvent<*>
            assertThat(payloadApplicationEvent.payload).isInstanceOf(TestDomainEvent::class.java)
        }
    }

    @Test
    fun `should not publish an event when transaction failed`(applicationEvents: ApplicationEvents) {
        // given
        val testDomainEvent = TestDomainEvent()

        // when
        try {
            transactionTemplate.executeWithoutResult {
                domainEvents.publish(testDomainEvent)
                throw UnexpectedRollbackException("test roll back")
            }
        } catch (_: Exception) {
        }

        // then
        assertThat(applicationEvents.stream().toList()).noneMatch { applicationEvent ->
            return@noneMatch (applicationEvent as? PayloadApplicationEvent<*>)?.payload is TestDomainEvent
        }
    }

    @Test
    fun `should not publish before commit`(applicationEvents: ApplicationEvents) {
        // given
        val testDomainEvent = TestDomainEvent()

        // when & then
        transactionTemplate.executeWithoutResult {
            domainEvents.publish(testDomainEvent)
            assertThat(applicationEvents.stream().toList()).noneMatch { applicationEvent ->
                return@noneMatch (applicationEvent as? PayloadApplicationEvent<*>)?.payload is TestDomainEvent
            }
        }
    }

    @Test
    fun `should publish event when there is no transaction running`(applicationEvents: ApplicationEvents) {
        // given
        val testDomainEvent = TestDomainEvent()

        // when
        domainEvents.publish(testDomainEvent)

        // then
        assertThat(applicationEvents.stream().toList()).satisfiesOnlyOnce { applicationEvent ->
            assertThat(applicationEvent).isInstanceOf(PayloadApplicationEvent::class.java)
            val payloadApplicationEvent = applicationEvent as PayloadApplicationEvent<*>
            assertThat(payloadApplicationEvent.payload).isInstanceOf(TestDomainEvent::class.java)
        }
    }

    private data class TestDomainEvent(
        val id: UUID = UUID.randomUUID()
    ) : DomainEvent
}
