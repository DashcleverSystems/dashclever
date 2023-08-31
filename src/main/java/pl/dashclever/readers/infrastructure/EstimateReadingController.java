package pl.dashclever.readers.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.dashclever.readers.domain.ReaderException;

import java.io.IOException;


@RestController
@RequestMapping("/api/estimate")
@RequiredArgsConstructor
public class EstimateReadingController {
    private final EstimateReading estimateReading;

    @PostMapping(
            value = "reader",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RepairInfo readEstimate(@RequestParam("file") final MultipartFile file) throws IOException, ReaderException {
        return estimateReading.retrieveRepairInfo(file.getInputStream());
    }
}
