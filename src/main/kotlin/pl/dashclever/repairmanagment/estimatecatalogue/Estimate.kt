package pl.dashclever.repairmanagment.estimatecatalogue

import jakarta.persistence.CascadeType
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.Version
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import org.hibernate.annotations.UuidGenerator
import pl.dashclever.publishedlanguage.SIZE_BETWEEN
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
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true) @JoinColumn(name = "estimate_id")
    val jobs: Set<Job> = emptySet(),
) {
    @Id @GeneratedValue @UuidGenerator
    val id: UUID? = null

    @Version @Suppress("UnusedPrivateMember")
    private val version: Long = 0
}
