package pl.dashclever.repairmanagment.plannig.readmodel

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.criteria.Root
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import pl.dashclever.commons.paging.PagingInfo
import pl.dashclever.commons.paging.Sort
import pl.dashclever.commons.paging.SortDirection
import pl.dashclever.commons.paging.SortDirection.ASC
import pl.dashclever.commons.paging.SortDirection.DESC
import pl.dashclever.commons.security.CurrentAccessProvider
import pl.dashclever.commons.time.LocalDateTimeHelper.asGmt
import pl.dashclever.repairmanagment.estimatecatalogue.Estimate
import pl.dashclever.repairmanagment.estimatecatalogue.EstimateRepository
import pl.dashclever.repairmanagment.plannig.infrastructure.repository.WorkshopPlan
import pl.dashclever.repairmanagment.plannig.model.Plan
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import org.springframework.data.domain.Sort as JpaSort

private val logger = KotlinLogging.logger { }

@Component
class PlanReader(
    private val planRepository: PlanJpaSpecificationExecutor,
    private val estimateRepository: EstimateRepository,
    private val currentAccessProvider: CurrentAccessProvider
) {

    data class PlanDto(
        val id: UUID,
        val estimateName: String,
        val estimateId: String,
        val technicalRepairTimeInMinutes: Int,
        val createdOn: ZonedDateTime?
    )

    data class PlanFilters(
        val createdAfter: LocalDateTime? = null,
        val estimateId: UUID? = null,
        val estimateName: String? = null,
        val sortDirection: SortDirection? = null,
        val hasRunningRepair: Boolean = false
    )

    fun findById(id: UUID): Optional<PlanDto> {
        val plan = planRepository.findById(id)
        val estimate = plan?.let { estimateRepository.findById(it.estimateId) }
        if (plan == null) {
            logger.warn { "Did not find plan with id: $id" }
            return Optional.empty()
        }
        if (estimate == null) {
            logger.warn { "Did not find estimate of a plan with id: $id" }
            return Optional.empty()
        }
        val planDto = PlanDto(
            id = plan.id,
            estimateId = estimate.id.toString(),
            estimateName = estimate.name,
            technicalRepairTimeInMinutes = estimate.jobs.filter { plan.getJobCatalogueIds().contains(it.id) }.sumOf { it.manMinutes },
            createdOn = plan.getCreationTimestamp()?.asGmt()
        )
        return Optional.of(planDto)
    }

    fun filter(filters: PlanFilters, pageRequest: PageRequest): PagingInfo<PlanDto> {
        val currentAccessWorkshop = this.currentAccessProvider.currentWorkshopId()
        val specification = listOfNotNull(
            Specifications.belongingToWorkshop(currentAccessWorkshop.workshopId),
            filters.createdAfter?.let { Specifications.createdAfter(it) },
            filters.estimateName?.let { Specifications.withEstimateNameLike(it) },
            filters.estimateId?.let { Specifications.withEstimateId(it) },
            Specifications.hasRunningRepair(filters.hasRunningRepair)
        ).reduce { acc, specification -> acc.and(specification) }
        val sortPageRequest = pageRequest.withSort(filters.getSort().toJpaSort())
        val plans: Page<Plan> = planRepository.findAll(specification, sortPageRequest)
        val planDtos: List<PlanDto> = plans.mapNotNull { plan ->
            val estimate = estimateRepository.findById(plan.estimateId)
            if (estimate == null) {
                logger.warn { "Could not fetch estimate of plan id: ${plan.id}" }
                return@mapNotNull null
            }
            return@mapNotNull PlanDto(
                id = plan.id,
                estimateName = estimate.name,
                estimateId = estimate.id.toString(),
                technicalRepairTimeInMinutes = estimate.jobs.filter { plan.getJobCatalogueIds().contains(it.id) }.sumOf { it.manMinutes },
                createdOn = plan.getCreationTimestamp()?.atZone(ZoneId.of("GMT"))
            )
        }
        return PagingInfo(
            totalElements = plans.totalElements,
            totalPages = plans.totalPages,
            pageNumber = plans.number,
            content = planDtos
        )
    }

    private fun PlanFilters.getSort(): Sort? {
        if (this.sortDirection == null) {
            return null
        }
        return Sort("createdOn", this.sortDirection)
    }

    private fun Sort?.toJpaSort(): JpaSort {
        if (this == null) {
            return JpaSort.unsorted()
        }
        return when (this.direction) {
            DESC -> JpaSort.by(Direction.DESC, this.field)
            ASC -> JpaSort.by(Direction.ASC, this.field)
        }
    }

    private object Specifications {

        fun createdAfter(time: LocalDateTime): Specification<Plan> =
            Specification { root, _, cb -> cb.greaterThanOrEqualTo(root.get("createdOn"), time) }

        fun withEstimateNameLike(estimateName: String): Specification<Plan> =
            Specification { root, query, cb ->
                val estimateRoot: Root<Estimate> = query.from(Estimate::class.java)
                val innerJoinPredicate = cb.equal(
                    root.get<UUID>("estimateId"),
                    estimateRoot.get<UUID>("id")
                )
                val estimateIdPredicate = cb.like(estimateRoot.get("name"), estimateName)
                cb.and(innerJoinPredicate, estimateIdPredicate)
            }

        fun withEstimateId(estimateId: UUID): Specification<Plan> =
            Specification { root, query, cb ->
                val estimateRoot: Root<Estimate> = query.from(Estimate::class.java)
                val innerJoinPredicate = cb.equal(
                    root.get<UUID>("estimateId"),
                    estimateRoot.get<UUID>("id")
                )
                val estimateIdPredicate = cb.equal(estimateRoot.get<UUID>("id"), estimateId)
                cb.and(innerJoinPredicate, estimateIdPredicate)
            }

        fun belongingToWorkshop(workshopId: UUID): Specification<Plan> =
            Specification { root, query, criteriaBuilder ->
                val securityRecordRoot: Root<WorkshopPlan> = query.from(WorkshopPlan::class.java)
                val innerJoinPredicate = criteriaBuilder.equal(
                    root.get<UUID>("id"),
                    securityRecordRoot.get<WorkshopPlan.ComposePk>("id").get<UUID>("planId")
                )
                val workshopIdPredicate = criteriaBuilder.equal(securityRecordRoot.get<WorkshopPlan.ComposePk>("id").get<UUID>("workshopId"), workshopId)
                criteriaBuilder.and(innerJoinPredicate, workshopIdPredicate)
            }

        fun hasRunningRepair(boolean: Boolean): Specification<Plan> =
            Specification { root, _, criteriaBuilder -> criteriaBuilder.equal(root.get<Boolean>("hasRunningRepair"), boolean) }
    }
}
