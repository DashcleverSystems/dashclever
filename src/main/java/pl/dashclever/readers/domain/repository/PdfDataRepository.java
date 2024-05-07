package pl.dashclever.readers.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dashclever.readers.reporting.PdfData;

import java.util.UUID;

@Repository
public interface PdfDataRepository extends JpaRepository<PdfData, UUID> {
}
