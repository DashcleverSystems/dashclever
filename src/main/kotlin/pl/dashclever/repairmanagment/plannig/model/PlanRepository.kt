package pl.dashclever.repairmanagment.plannig.model

import java.util.UUID

interface PlanRepository {

    fun save(plan: Plan): Plan

    fun findById(id: UUID): Plan?
}
