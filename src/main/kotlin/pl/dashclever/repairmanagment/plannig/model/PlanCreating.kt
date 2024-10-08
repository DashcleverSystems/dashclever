package pl.dashclever.repairmanagment.plannig.model

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository
import java.util.UUID

@Service
class PlanCreating(
    private val estimateRepository: EstimateRepository,
    private val planRepository: PlanRepository
) {

    @Transactional
    fun create(estimateId: UUID): UUID {
        val estimate = estimateRepository.findById(estimateId)
            ?: throw ResponseStatusException(NOT_FOUND)
        val plan = PlanFactory.create(
            estimateId = estimate.id,
            jobs = estimate.jobs.associate { it.id!! to it.manMinutes }
        )
        return planRepository.save(plan).id
    }
}
