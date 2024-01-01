package pl.dashclever.repairmanagment.plannig.model

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.util.*

@Component
@Transactional
class AssignJobHandler(
    private val planRepository: PlanRepository
) {

    data class AssignJob(
        override val planId: UUID,
        val employeeId: String,
        val catalogueJobId: Long,
        val at: LocalDate,
        val hour: Int?
    ) : PlanCommand

    fun handle(command: AssignJob) {
        val plan = planRepository.findById(command.planId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        if (command.hour == null) {
            plan.assign(command.catalogueJobId, command.employeeId, command.at)
        } else {
            plan.assignWithTime(command.catalogueJobId, command.employeeId, command.at, command.hour)
        }
        planRepository.save(plan)
    }

    fun handle(commands: Set<AssignJob>) {
        for (command in commands) {
            handle(command)
        }
    }
}
