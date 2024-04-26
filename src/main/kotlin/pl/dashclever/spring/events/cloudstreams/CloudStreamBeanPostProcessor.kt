package pl.dashclever.spring.events.cloudstreams

import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.cloud.function.context.FunctionCatalog
import org.springframework.cloud.function.context.FunctionProperties
import org.springframework.context.ApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.stereotype.Component

@Component
internal class CloudStreamBeanPostProcessor(
    private val applicationContext: ApplicationContext,
    private val environment: ConfigurableEnvironment
) : BeanPostProcessor {

    var initialized = false

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        if (bean is FunctionCatalog) {
            // make sure only called once to not register multiple property sources (just for performance)
            if (!initialized) {
                environment.propertySources.addFirst(createCloudStreamProperties())
                initialized = true
            }
        }
        return bean
    }

    private fun createCloudStreamProperties(): PropertySource<*> {
        val beanNames = listOf(
            CloudStreamConsumer::class.java,
            CloudStreamFunction::class.java,
            TransactionlessCloudStreamConsumer::class.java,
            TransactionlessCloudStreamFunction::class.java
        ).flatMap { applicationContext.getBeanNamesForType(it).toList() }
        val beanNamesAsString = beanNames.joinToString(";")
        return MapPropertySource(
            "cloudStream",
            mapOf(
                FunctionProperties.FUNCTION_DEFINITION to beanNamesAsString
            )
        )
    }
}
