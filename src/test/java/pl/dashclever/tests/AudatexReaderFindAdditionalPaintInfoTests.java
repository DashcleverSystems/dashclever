package pl.dashclever.tests;

import org.junit.jupiter.api.Test;
import pl.dashclever.readers.domain.EstimateReader;
import pl.dashclever.readers.domain.EstimateReaderFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AudatexReaderFindAdditionalPaintInfoTests {

    @Test
    public void ReturnsProperAdditionalPaintInfo_AfterMockBmwEstimateIsRead() throws IOException {
        EstimateReader audatexReader = new EstimateReaderFactory(ToInputStreamConverter.fromString(MockWholeEstimate.WholeEstimate))
                .withCustomStreamExtractor(new MockEstimateStringStreamExtractor());
        //WHEN
        var paintInfo = audatexReader.FindPaintInfo();
        assertTrue(paintInfo.baseColorWithCode().contains("czarna"));
        assertTrue(paintInfo.baseColorWithCode().contains("perła"));
        assertTrue(paintInfo.baseColorWithCode().contains("475"));
        assertEquals(2, paintInfo.varnishingPaintInfo().size());
        assertTrue(paintInfo.varnishingPaintInfo().stream().anyMatch(line -> line.contains("wodny")));
        assertTrue(paintInfo.varnishingPaintInfo().stream().anyMatch(line -> line.contains("2")));
        assertTrue(paintInfo.varnishingPaintInfo().stream().anyMatch(line -> line.contains("warstw")));
        assertTrue(paintInfo.varnishingPaintInfo().stream().anyMatch(line -> line.contains("mineral")));
    }
}
