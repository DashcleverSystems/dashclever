package pl.dashclever.readers.reporting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.dashclever.readers.infrastructure.RepairInfo;
import pl.dashclever.readers.reporting.repository.PdfGeneratedModelRepository;
import pl.dashclever.readers.reporting.repository.ReportRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ReportingService {
    final ObjectMapper mapper = new ObjectMapper();
    final ReportRepository reportRepository;
    final PdfGeneratedModelRepository pdfRepository;

    public void saveReport(ReportDto dto) {
        var report = dto.toEntity();
        reportRepository.save(report);
    }

    public void savePdfGeneratedModel(MultipartFile file, RepairInfo repairInfo) throws IOException {
        var generatedData = mapper.writeValueAsString(repairInfo);
        pdfRepository.save(new PdfGeneratedModel(repairInfo.reportingId(), file, generatedData));
    }

    public RepairInfo retrieveGeneratedData(PdfGeneratedModel model) throws JsonProcessingException {
        return mapper.readValue(model.getGeneratedData(), RepairInfo.class);
    }
}
