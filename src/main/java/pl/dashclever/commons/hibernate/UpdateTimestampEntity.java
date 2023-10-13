package pl.dashclever.commons.hibernate;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public abstract class UpdateTimestampEntity<T> extends CreationTimestampEntity<T> {

    @UpdateTimestamp
    @Column(name = "last_modified_on")
    private LocalDateTime lastModifiedOn;
}
