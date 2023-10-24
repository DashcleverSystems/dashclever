package pl.dashclever.commons.security

import java.util.UUID

interface EntitySecurityRecordRepository<T, ENTITY_ID, E : SecurityRecord> {

    fun create(securityRecord: E): E

    fun doesSecurityRecordExistFor(entity: T): Boolean

    fun deleteByEntityId(entityId: ENTITY_ID)
}

interface SecurityRecord

interface WorkshopSecurityRecord : SecurityRecord {
    val workshopId: UUID
}
