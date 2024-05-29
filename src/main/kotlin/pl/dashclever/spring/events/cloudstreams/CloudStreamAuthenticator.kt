package pl.dashclever.spring.events.cloudstreams

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import pl.dashclever.commons.security.SystemAccessSetter
import pl.dashclever.commons.security.SystemOnBehalfOfWorkshop
import pl.dashclever.spring.events.cloudstreams.CloudStreamDomainEventsMultitenantProxy.MultitenantDomainEvent

private val logger = KotlinLogging.logger {}

@Aspect
@Component
class CloudStreamAuthenticator(
    private val systemAccessSetter: SystemAccessSetter
) {

    @Pointcut("target(CloudStreamFunction) || target(TransactionlessCloudStreamConsumer) || target(CloudStreamConsumer) || target(TransactionlessCloudStreamConsumer)")
    fun authenticateCloudStream() {}

    @Before("authenticateCloudStream()")
    fun authenticate(joinPoint: JoinPoint) {
        logger.info { "Considering cloud stream ${joinPoint.target} to authentication." }
        if (joinPoint.args.any { it is MultitenantDomainEvent<*> }) {
            val event = joinPoint.args.first { it is MultitenantDomainEvent<*> } as MultitenantDomainEvent<*>
            logger.info { "Authenticating cloud stream ${joinPoint.target} on behalf of workshop ${event.workshopId}." }
            systemAccessSetter.set(SystemOnBehalfOfWorkshop(event.workshopId))
        }
    }
}
