package pl.dashclever.commons.hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import org.jetbrains.annotations.Nullable;

@MappedSuperclass
public abstract class OptimisticLockEntity<T> extends UpdateTimestampEntity<T> {

    @Version
    @Column(name = "version")
    @Nullable
    private Long version;
}
