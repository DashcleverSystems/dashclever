package pl.dashclever.readers.reporting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pl.dashclever.commons.hibernate.CreationTimestampEntity;
import pl.dashclever.commons.hibernate.OptimisticLockEntity;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "READERS_REPORTING_REPORT")
@NoArgsConstructor
public class Report extends CreationTimestampEntity<UUID> {
    @Id
    private UUID id = UUID.randomUUID();
    @NotNull
    @Column(name = "reporting_id")
    private UUID reportingId;
    @Length(min = 1, max = 1000)
    private String description;

    public Report(UUID reportingId, String description) {
        this.reportingId = reportingId;
        this.description = description;
    }

    @Override
    protected UUID getIdentifierValue() {
        return this.id;
    }
}
