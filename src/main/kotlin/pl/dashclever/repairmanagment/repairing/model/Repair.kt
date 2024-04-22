package pl.dashclever.repairmanagment.repairing.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import pl.dashclever.commons.hibernate.OptimisticLockEntity
import java.util.UUID

@Entity
@Table(name = "RM_REPAIRING_REPAIR")
class Repair(
    val planId: UUID
) : OptimisticLockEntity<UUID>() {

    @Id
    val id: UUID = UUID.randomUUID()

    override fun getIdentifierValue(): UUID = id
}
