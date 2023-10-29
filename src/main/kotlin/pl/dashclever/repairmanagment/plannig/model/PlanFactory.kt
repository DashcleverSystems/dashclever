package pl.dashclever.repairmanagment.plannig.model

private typealias JobIdWithManMinutes = Map<Long, Int>

object PlanFactory {

    fun create(estimateId: String, jobs: JobIdWithManMinutes): Plan {
        return Plan(
            estimateId = estimateId,
            jobs = jobs.map { Job(it.key, it.value) }.toSet()
        )
    }
}
