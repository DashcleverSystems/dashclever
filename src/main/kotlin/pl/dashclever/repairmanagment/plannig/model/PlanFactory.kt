package pl.dashclever.repairmanagment.plannig.model

object PlanFactory {

    fun create(estimateId: String, jobs: Map<Long, Int>): Plan {
        return Plan(
            estimateId = estimateId,
            jobs = jobs.map { Job(it.key, it.value) }.toSet()
        )
    }
}
