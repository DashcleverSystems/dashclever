package pl.dashclever.commons.hibernate;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
public abstract class UpdateTimestampEntity<T> extends CreationTimestampEntity<T> {

    @LastModifiedDate
    @Column(name = "last_modified_on")
    @Nullable
    private LocalDateTime lastModifiedOn;
}
