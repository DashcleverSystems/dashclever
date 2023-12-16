package pl.dashclever.repairmanagment.estimatecatalogue

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import pl.dashclever.commons.hibernate.OptimisticLockEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "RM_ESTIMATECATALOGUE_ESTIMATE")
class Estimate(

    val estimateId: String,
    val vehicleInfo: VehicleInfo,
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
