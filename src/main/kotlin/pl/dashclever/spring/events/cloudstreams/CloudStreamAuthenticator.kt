package pl.dashclever.spring.events.cloudstreams

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import pl.dashclever.commons.security.SystemAccessSetter
import pl.dashclever.commons.security.SystemOnBehalfOfWorkshop
import pl.dashclever.spring.events.cloudstreams.CloudStreamDomainEventsMultitenantProxy.MultitenantDomainEvent

@Aspect
@Component
class CloudStreamAuthenticator(
    private val systemAccessSetter: SystemAccessSetter
) {

    @Pointcut("target(CloudStreamFunction) || target(TransactionlessCloudStreamConsumer) || target(CloudStreamConsumer) || target(TransactionlessCloudStreamConsumer)")
    fun authenticateCloudStream() {}

    @Before("authenticateCloudStream()")
    fun authenticate(joinPoint: JoinPoint) {
        if (joinPoint.args.any { it is MultitenantDomainEvent<*> }) {
            val event = joinPoint.args.first { it is MultitenantDomainEvent<*> } as MultitenantDomainEvent<*>
            systemAccessSetter.set(SystemOnBehalfOfWorkshop(event.workshopId))
        }
    }
}
