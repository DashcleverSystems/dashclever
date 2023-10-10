package pl.dashclever.commons.hibernate

import jakarta.persistence.Version
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

abstract class EntityBase<ID> : WithId<ID>, WithCreationTimestamp, WithLastModificationTimestamp {

    abstract override val id: ID?

    @Version
    private val version: Int = 0

    @CreationTimestamp
    override val createdOn: LocalDateTime? = null

    @UpdateTimestamp
    override val lastModifiedOn: LocalDateTime? = null
}
