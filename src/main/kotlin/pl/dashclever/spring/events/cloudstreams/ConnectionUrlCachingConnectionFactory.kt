package pl.dashclever.spring.events.cloudstreams

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConnectionUrlCachingConnectionFactory(
    private val cachingConnectionFactory: CachingConnectionFactory,
    @Value("\${spring.rabbitmq.connection-url}")
    private val rabbitMqConnectionString: String
) {

    @Bean
    fun connection() {
        val rabbitConnectionProperties = ConnectionUrlParser(rabbitMqConnectionString)
        cachingConnectionFactory.apply {
            setHost(rabbitConnectionProperties.host)
            rabbitConnectionProperties.virtualHost?.let { virtualHost = it }
            port = rabbitConnectionProperties.port
            username = rabbitConnectionProperties.username
            setPassword(rabbitConnectionProperties.password)
        }
    }
}
