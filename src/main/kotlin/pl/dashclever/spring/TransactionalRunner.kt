package pl.dashclever.spring

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TransactionalRunner {

    @Transactional
    fun <T> run(action: () -> T): T {
        return action()
    }
}
