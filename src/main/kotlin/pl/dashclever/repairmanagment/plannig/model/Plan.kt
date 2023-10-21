package pl.dashclever.repairmanagment.plannig.model

import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.EAGER
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import pl.dashclever.commons.hibernate.OptimisticLockEntity
import pl.dashclever.publishedlanguage.DomainException
import pl.dashclever.repairmanagment.plannig.model.PlanEvent.TaskAssigned
import java.time.LocalDate
import java.util.UUID
import kotlin.math.ceil
import kotlin.math.roundToLong

@Entity
@Table(name = "RM_PLANNING_PLAN")
@Suppress("MagicNumber")
class Plan internal constructor(
    @Id
    val id: UUID = UUID.randomUUID(),
    val estimateId: String,
    @OneToMany(cascade = [ALL], orphanRemoval = true, fetch = EAGER)
    @JoinColumn(name = "plan_id")
    private val jobs: Set<Job>,
) : OptimisticLockEntity<UUID>() {

    fun assign(jobId: Long, employeeId: String, at: LocalDate): TaskAssigned {
        val job = tryFindJob(jobId)
        if (isNoneJobAssigned()) {
            return assign(job, employeeId, at)
        }
        if (isAfterLastRepairDay(at)) {
            throw DomainException("Can not assign task to be done after estimated technical repair time")
        }
        return assign(job, employeeId, at)
    }

    fun assignWithTime(jobId: Long, employeeId: String, at: LocalDate, hour: Int): TaskAssigned {
        if (!isWithinWorkingHours(hour)) {
            throw DomainException("It is not possible to assign job not within working hours")
        }
        val job = tryFindJob(jobId)
        if (isNoneJobAssigned()) {
            return assign(job, employeeId, at, hour)
        }
        if (isAfterLastRepairDay(at)) {
            throw DomainException("Can not assign task to be done after estimated technical repair time")
        }
        return assign(job, employeeId, at, hour)
    }

    private fun tryFindJob(jobId: Long): Job = jobs.first { it.catalogueJobId == jobId }

    private fun isNoneJobAssigned() =
        this.jobs.none { it.isAssigned() }

    private fun assign(job: Job, employeeId: String, at: LocalDate, hour: Int? = null): TaskAssigned {
        return if (hour == null) {
            job.assign(employeeId, at)
            TaskAssigned(this.id.toString(), job.catalogueJobId.toString(), employeeId)
        } else {
            job.assign(employeeId, at, hour)
            TaskAssigned(this.id.toString(), job.catalogueJobId.toString(), employeeId)
        }
    }

    private fun isAfterLastRepairDay(date: LocalDate) =
        date.isAfter(lastDayOfRepair())

    private fun lastDayOfRepair(): LocalDate {
        val earliestTask = this.jobs.filter { it.assignedAt != null }
            .minByOrNull { it.assignedAt!! }
        return earliestTask!!.assignedAt!!.plusDays(technicalRepairTime()).minusDays(1L)
    }

    private fun isWithinWorkingHours(hour: Int) =
        !(hour < 8 || hour > 18)

    private fun technicalRepairTime(): Long =
        ceil(jobs.sumOf { it.manMinutes } / 60F / 8F / 0.7F).roundToLong()

    override fun getIdentifierValue(): UUID = this.id
}
