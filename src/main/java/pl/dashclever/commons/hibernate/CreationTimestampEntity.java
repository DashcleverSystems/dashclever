package pl.dashclever.commons.hibernate;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;

@MappedSuperclass
public abstract class CreationTimestampEntity<T> extends ConstHashCodeEntity<T> {

    @CreationTimestamp
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    protected LocalDateTime getCreatedOn() {
        return this.createdOn;
    }
}
