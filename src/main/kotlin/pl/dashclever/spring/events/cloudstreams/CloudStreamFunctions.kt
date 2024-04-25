package pl.dashclever.spring.events.cloudstreams

import org.springframework.transaction.annotation.Transactional
import java.util.function.Consumer
import java.util.function.Function

/**

Warning: When creating cloud stream Consumers/Functions as @Bean methods
in @Configuration classes, you must use one of these 4 interfaces. You must not
create your own generic child interface as that will break Spring Cloud Stream's message
type inference.
When creating a cloud stream consumer/function as a (non-generic) @Component, you can of
course create a child class.
 */
fun interface TransactionlessCloudStreamConsumer<T> : Consumer<T>

fun interface TransactionlessCloudStreamFunction<R, T> : Function<R, T>

@Transactional
fun interface CloudStreamConsumer<T> : Consumer<T>

@Transactional
fun interface CloudStreamFunction<R, T> : Function<R, T>
