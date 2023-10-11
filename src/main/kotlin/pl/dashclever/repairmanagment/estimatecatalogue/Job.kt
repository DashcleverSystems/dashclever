package pl.dashclever.repairmanagment.estimatecatalogue

import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.validation.constraints.Size
import pl.dashclever.commons.hibernate.EntityBase
import pl.dashclever.publishedlanguage.Money
import pl.dashclever.publishedlanguage.SIZE_MAX

@Entity(name = "RM_ESTIMATECATALOGUE_JOB")
@Table(name = "RM_ESTIMATECATALOGUE_JOB")
data class Job(
    @field:Size(max = 512, message = "$SIZE_MAX;512") val name: String,
    val manMinutes: Int,
    @Embedded val worth: Money,
    @Enumerated(STRING) val jobType: JobType,
) : EntityBase<Long>() {

    private companion object {

        const val ID_GENERATOR_NAME = "rm_estimatecatalogue_job_id_generator"
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GENERATOR_NAME)
    @SequenceGenerator(name = ID_GENERATOR_NAME, sequenceName = "${ID_GENERATOR_NAME}_sequence", allocationSize = 50)
    override val id: Long? = null
}
