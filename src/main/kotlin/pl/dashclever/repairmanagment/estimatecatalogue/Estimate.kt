package pl.dashclever.repairmanagment.estimatecatalogue

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.EAGER
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import pl.dashclever.commons.hibernate.OptimisticLockEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "RM_ESTIMATECATALOGUE_ESTIMATE")
class Estimate(

    val name: String,
    val customerName: String,
    val vehicleInfo: VehicleInfo,
    val paintInfo: PaintInfo,
    var startDate: LocalDate?,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = EAGER)
    @JoinColumn(name = "estimate_id")
    val jobs: Set<Job> = emptySet(),
    @OneToMany(mappedBy = "estimate", cascade = [CascadeType.ALL], orphanRemoval = true)
    val reports: Set<EstimateReport> = emptySet()
) : OptimisticLockEntity<UUID>() {

    fun getCreationTimestamp(): LocalDateTime? = super.getCreatedOn()

    @Id
    val id: UUID = UUID.randomUUID()
    override fun getIdentifierValue(): UUID = this.id
}
