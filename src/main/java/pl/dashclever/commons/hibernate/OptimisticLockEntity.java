package pl.dashclever.commons.hibernate;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class OptimisticLockEntity<T> extends UpdateTimestampEntity<T> {

    @Version
    @Column(name = "version")
    private Long version;
}
