package pl.dashclever.readers.reporting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dashclever.readers.reporting.PdfGeneratedModel;

import java.util.UUID;

@Repository
public interface PdfGeneratedModelRepository extends JpaRepository<PdfGeneratedModel, UUID> {
}
