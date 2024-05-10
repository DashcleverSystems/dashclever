package pl.dashclever.readers.reporting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;
import pl.dashclever.commons.hibernate.CreationTimestampEntity;
import pl.dashclever.commons.hibernate.OptimisticLockEntity;
import pl.dashclever.readers.infrastructure.RepairInfo;

import java.io.IOException;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "READERS_REPORTING_PDF_GENERATED_MODEL")
@NoArgsConstructor
public class PdfGeneratedModel extends CreationTimestampEntity<UUID> {
    @Id
    private UUID id;
    @NotNull
    @Column(name = "file_data")
    private byte[] fileData;

    @NotNull
    @Column(name = "generated_data")
    private String generatedData;

    public PdfGeneratedModel(UUID reportingId, MultipartFile file, String generatedData) throws IOException {
        this.id = reportingId;
        this.fileData = file.getBytes();
        this.generatedData = generatedData;
    }

    @Override
    protected UUID getIdentifierValue() {
        return this.id;
    }
}
