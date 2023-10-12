package pl.dashclever.commons.hibernate;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class BaseEntity<T> {

    @Version
    @Column(name = "version")
    private Long version;

    @CreationTimestamp
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "last_modified_on")
    private LocalDateTime lastModifiedOn;

    public abstract T getIdentifier();

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseEntity<T> other = (BaseEntity<T>) obj;
        return this.getIdentifier() != null && this.getIdentifier().equals(other.getIdentifier());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
