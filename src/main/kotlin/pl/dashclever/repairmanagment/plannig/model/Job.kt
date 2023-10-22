package pl.dashclever.repairmanagment.plannig.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import pl.dashclever.commons.hibernate.UpdateTimestampEntity
import java.time.LocalDate

@Entity(name = "RM_PLANNING_JOB")
@Table(name = "RM_PLANNING_JOB")
internal class Job(
    val catalogueJobId: Long,
    val manMinutes: Int
) : UpdateTimestampEntity<Long>() {

    private var assignedTo: String? = null
    var assignedAt: LocalDate? = null; private set
    private var hour: Int? = null

    fun isAssigned() = assignedTo != null

    fun assign(employeeId: String, at: LocalDate) {
        this.assignedTo = employeeId
        this.assignedAt = at
    }

    fun assign(employeeId: String, at: LocalDate, time: Int) {
        this.assignedTo = employeeId
        this.assignedAt = at
        this.hour = time
    }

    private companion object {

        const val ID_GENERATOR_NAME = "rm_planning_job_id_generator"
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GENERATOR_NAME)
    @SequenceGenerator(name = ID_GENERATOR_NAME, sequenceName = "${ID_GENERATOR_NAME}_sequence", allocationSize = 50)
    val id: Long? = null
    override fun getIdentifierValue(): Long? = this.id
}
