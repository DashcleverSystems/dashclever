package pl.dashclever.readers;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pl.dashclever.commons.hibernate.OptimisticLockEntity;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "RM_ESTIMATECATALOGUE_REPORT")
@NoArgsConstructor
public class Report extends OptimisticLockEntity<UUID> {
    @Id
    private UUID id = UUID.randomUUID();
    @NotNull
    @Column(name = "pdf_name")
    private String pdfName;
    @Length(min = 1, max = 1000)
    private String content;

    public Report(String pdfName, String content) {
        this.pdfName = pdfName;
        this.content = content;
    }

    @Override
    protected UUID getIdentifierValue() {
        return this.id;
    }
}
