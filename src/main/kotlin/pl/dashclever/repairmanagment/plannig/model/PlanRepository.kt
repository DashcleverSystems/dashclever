package pl.dashclever.repairmanagment.plannig.model

import java.util.UUID

interface PlanRepository {

    fun save(plan: Plan): Plan

    fun findById(id: UUID): Plan?

    fun findByIdOrThrow(id: UUID): Plan

    fun findAllByEstimateId(estimateId: UUID): Set<Plan>
}
