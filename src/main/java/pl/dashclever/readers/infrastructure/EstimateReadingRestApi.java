package pl.dashclever.readers.infrastructure;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.dashclever.readers.domain.ReaderException;

import java.io.IOException;


@RestController
@RequestMapping("/api/estimate")
@RequiredArgsConstructor
@Tag(name = "estimate-api")
public class EstimateReadingRestApi {
    private final EstimateReading estimateReading;
    private final EstimateSaver estimateSaver;

    @PostMapping(
        value = "reader",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RepairInfo readEstimate(@RequestParam("file") final MultipartFile file) throws IOException, ReaderException {
        var repairInfo = estimateReading.retrieveRepairInfo(file.getInputStream());
        estimateSaver.savePDF(file, repairInfo.reportId());
        return repairInfo;
    }

}
