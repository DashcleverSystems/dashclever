package pl.dashclever.repairmanagment.estimatecatalogue

import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import pl.dashclever.commons.hibernate.OptimisticLockEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "RM_ESTIMATECATALOGUE_ESTIMATEREPORT")
class EstimateReport(
    val pdfName: String,
    @NotBlank
    @Size(max = 1000)
    val content: String,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "estimate_id")
    val estimate: Estimate
) : OptimisticLockEntity<UUID>() {

    fun getCreationTimestamp(): LocalDateTime? = super.getCreatedOn()

    @Id
    val id: UUID = UUID.randomUUID()
    override fun getIdentifierValue(): UUID = this.id
}
