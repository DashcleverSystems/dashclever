package pl.dashclever.readers.reporting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import pl.dashclever.commons.hibernate.OptimisticLockEntity;

import java.io.IOException;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "READERS_REPORTING_PDF_DATA")
@NoArgsConstructor
public class PdfData extends OptimisticLockEntity<UUID> {
    @Id
    private UUID id = UUID.randomUUID();
    @NotNull
    @Column(name = "reporting_id")
    private UUID reportingId;
    @NotNull
    @Column(name = "file_data")
    private byte[] fileData;

    public PdfData(UUID reportingId, MultipartFile file) throws IOException {
        this.reportingId = reportingId;
        this.fileData = file.getBytes();
    }

    @Override
    protected UUID getIdentifierValue() {
        return this.id;
    }
}
