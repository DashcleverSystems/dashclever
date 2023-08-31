package pl.dashclever.readers.domain;

import pl.dashclever.readers.domain.audatex.AudatexReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EstimateReaderFactory {

    private final InputStream estimateStream;

    public EstimateReaderFactory(InputStream estimateStream) {
        this.estimateStream = estimateStream;
    }

    public EstimateReader withCustomStreamExtractor(EstimateStreamExtractor streamExtractor) throws IOException {
        List<String> estimatePages = streamExtractor.extractPages(this.estimateStream);
        return new AudatexReader(estimatePages);
    }

    public EstimateReader withPdfStreamExtractor() throws IOException {
        EstimateStreamExtractor estimatePdfStreamExtractor = new EstimatePdfStreamExtractor();
        List<String> estimatePages = estimatePdfStreamExtractor.extractPages(this.estimateStream);
        return new AudatexReader(estimatePages);
    }
}
