package pl.dashclever.readers.infrastructure;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
class EstimateSaver {
    final String UPLOAD_DIRECTORY = "src/main/resources/pdf/";

    void savePDF(MultipartFile file, String uniqueId) throws IOException {
        var transformedId = uniqueId.replaceAll("/", "-");
        var bytes = file.getBytes();

        Path path = Paths.get(UPLOAD_DIRECTORY + transformedId + ".pdf");

        Files.write(path, bytes);
    }
}
