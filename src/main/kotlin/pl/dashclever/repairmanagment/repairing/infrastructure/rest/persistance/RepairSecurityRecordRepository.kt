package pl.dashclever.repairmanagment.repairing.infrastructure.rest.persistance

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component
import pl.dashclever.commons.security.EntitySecurityRecordRepository
import pl.dashclever.commons.security.WorkshopSecurityRecord
import pl.dashclever.repairmanagment.repairing.model.Repair
import java.io.Serializable
import java.util.UUID

private const val securityRecordTable = "RM_REPAIRING_SR_REPAIR_WORKSHOP"

@Component
interface RepairSecurityRecordRepository :
    EntitySecurityRecordRepository<Repair, UUID, RepairWorkshop>,
    Repository<RepairWorkshop, RepairWorkshop.ComposePk> {

    override fun create(securityRecord: RepairWorkshop): RepairWorkshop = save(securityRecord)

    fun save(securityRecord: RepairWorkshop): RepairWorkshop

    override fun doesSecurityRecordExistFor(entity: Repair): Boolean = existsByRepairId(entity.id)

    @Query(
        value = """
            SELECT
                CASE WHEN EXISTS (
                    SELECT 1
                    FROM $securityRecordTable sr
                    WHERE sr.repair_id = :repairId
                )
            THEN 'true'
            ELSE 'false'
            END
    """,
        nativeQuery = true
    )
    fun existsByRepairId(repairId: UUID): Boolean

    override fun deleteByEntityId(entityId: UUID) =
        deleteByRepairId(entityId)

    @Modifying
    @Query("DELETE FROM RepairWorkshop WHERE id.repairId = :repairId")
    fun deleteByRepairId(repairId: UUID)
}

@Entity
@Table(name = securityRecordTable)
class RepairWorkshop(
    workshopId: UUID,
    repairId: UUID
) : WorkshopSecurityRecord {

    override val workshopId: UUID get() = id.workshopId
    val repairId get() = id.repairId

    @EmbeddedId
    private val id: ComposePk = ComposePk(workshopId, repairId)

    @Embeddable
    data class ComposePk(
        @Column(name = "workshop_id")
        val workshopId: UUID,
        @Column(name = "repair_id")
        val repairId: UUID
    ) : Serializable {

        companion object {

            const val serialVersionUID = 48L
        }
    }
}
