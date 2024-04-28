package pl.dashclever.spring.events.cloudstreams

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.annotation.Configuration

private const val UNDEFINED_CONNECTION_URL = "undefined"

@Configuration
class ConnectionUrlCachingConnectionFactory(
    @Value("\${spring.rabbitmq.connection-url:$UNDEFINED_CONNECTION_URL}")
    private val rabbitMqConnectionString: String
) : BeanPostProcessor {

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        if (bean is CachingConnectionFactory && rabbitMqConnectionString != UNDEFINED_CONNECTION_URL) {
            val connectionProperties = ConnectionUrlParser(rabbitMqConnectionString)
            bean.setHost(connectionProperties.host)
            if (connectionProperties.virtualHost != null) {
                bean.virtualHost = connectionProperties.virtualHost
            }
            bean.port = connectionProperties.port
            bean.username = connectionProperties.username
            bean.setPassword(connectionProperties.password)
        }
        return super.postProcessAfterInitialization(bean, beanName)
    }
}
