package pl.dashclever.repairmanagment.estimatecatalogue

import jakarta.persistence.CascadeType
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import pl.dashclever.commons.hibernate.OptimisticLockEntity
import pl.dashclever.publishedlanguage.SIZE_BETWEEN
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "RM_ESTIMATECATALOGUE_ESTIMATE")
class Estimate(
    @field:Size(min = 1, max = 24, message = "$SIZE_BETWEEN;1;24")
    val estimateId: String,
    @field:Valid @Embedded
    val vehicleInfo: VehicleInfo,
    @field:Valid @Embedded
    val paintInfo: PaintInfo,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "estimate_id")
    val jobs: Set<Job> = emptySet()
) : OptimisticLockEntity<UUID>() {

    fun getCreationTimestamp(): LocalDateTime = super.getCreatedOn()

    @Id
    val id: UUID = UUID.randomUUID()
    override fun getIdentifierValue(): UUID = this.id
}
