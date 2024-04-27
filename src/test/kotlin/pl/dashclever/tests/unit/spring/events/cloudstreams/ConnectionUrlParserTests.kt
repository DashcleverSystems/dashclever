package pl.dashclever.tests.unit.spring.events.cloudstreams

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.dashclever.spring.events.cloudstreams.ConnectionUrlParser

internal class ConnectionUrlParserTests {

    @Test
    fun `should parse a connection url without port`() {
        // given
        val connectionUrl = "amqps://fyqpugxz:tHj0lF-HmRshBgjYVcGqYz1qxS7OlhVU@cro2w.rmq.cloudamqp.com/fyqpsgvz"

        // when
        val properties = ConnectionUrlParser(connectionUrl)

        // the
        assertThat(properties.host).isEqualTo("cro2w.rmq.cloudamqp.com")
        assertThat(properties.virtualHost).isEqualTo("fyqpsgvz")
        assertThat(properties.username).isEqualTo("fyqpugxz")
        assertThat(properties.password).isEqualTo("tHj0lF-HmRshBgjYVcGqYz1qxS7OlhVU")
    }

    @Test
    fun `should parse connection url with port`() {
        // given
        val connectionUrl = "amqps://admin:admin@localhost:9999/fyqpsgvz"

        // when
        val properties = ConnectionUrlParser(connectionUrl)

        // the
        assertThat(properties.host).isEqualTo("localhost")
        assertThat(properties.port).isEqualTo(9999)
        assertThat(properties.virtualHost).isEqualTo("fyqpsgvz")
        assertThat(properties.username).isEqualTo("admin")
        assertThat(properties.password).isEqualTo("admin")
    }
}
