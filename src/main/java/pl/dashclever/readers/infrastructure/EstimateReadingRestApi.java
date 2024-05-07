package pl.dashclever.readers.infrastructure;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.dashclever.readers.domain.repository.PdfDataRepository;
import pl.dashclever.readers.reporting.PdfData;
import pl.dashclever.readers.reporting.ReportDto;
import pl.dashclever.readers.domain.ReaderException;
import pl.dashclever.readers.domain.repository.ReportRepository;

import java.io.IOException;


@RestController
@RequestMapping("/api/estimate")
@RequiredArgsConstructor
@Tag(name = "estimate-api")
public class EstimateReadingRestApi {
    private final EstimateReading estimateReading;
    private final ReportRepository reportRepository;
    private final PdfDataRepository pdfDataRepository;

    @PostMapping(
        value = "reader",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RepairInfo readEstimate(@RequestParam("file") final MultipartFile file) throws IOException, ReaderException {
        var repairInfo = estimateReading.retrieveRepairInfo(file.getInputStream());
        pdfDataRepository.save(new PdfData(repairInfo.reportingId(), file));
        return repairInfo;
    }

    @PostMapping("report")
    public ResponseEntity<HttpStatus> createReport(@RequestBody ReportDto reportDto) {
        reportRepository.save(reportDto.toEntity());
        return ResponseEntity.ok().build();
    }

}
