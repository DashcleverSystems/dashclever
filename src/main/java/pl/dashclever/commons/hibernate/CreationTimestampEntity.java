package pl.dashclever.commons.hibernate;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CreationTimestampEntity<T> extends ConstHashCodeEntity<T> {

    @CreatedDate
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    protected LocalDateTime getCreatedOn() {
        return this.createdOn;
    }
}
