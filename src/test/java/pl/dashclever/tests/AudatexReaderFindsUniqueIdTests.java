package pl.dashclever.tests;

import org.junit.jupiter.api.Test;
import pl.dashclever.readers.domain.EstimateReader;
import pl.dashclever.readers.domain.EstimateReaderFactory;
import pl.dashclever.readers.domain.ReaderException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class AudatexReaderFindsUniqueIdTests {

    @Test
    void findsUniqueEstimateId() throws IOException, ReaderException {
        EstimateReader audatexReader = new EstimateReaderFactory(ToInputStreamConverter.fromString(MockWholeEstimate.WholeEstimate))
                .withCustomStreamExtractor(new MockEstimateStringStreamExtractor());
        //WHEN
        var uniqueEstimateId = audatexReader.findUniqueNr();
        assertFalse(uniqueEstimateId.isBlank());
    }
}
