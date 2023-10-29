package pl.dashclever.repairmanagment.plannig.model

import java.util.UUID

private typealias JobIdWithManMinutes = Map<Long, Int>

object PlanFactory {

    fun create(estimateId: UUID, jobs: JobIdWithManMinutes): Plan {
        return Plan(
            id = UUID.randomUUID(),
            estimateId = estimateId,
            jobs = jobs.map { Job(it.key, it.value) }.toSet()
        )
    }
}
