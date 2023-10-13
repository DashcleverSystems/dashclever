package pl.dashclever.readers.domain;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public interface EstimateStreamExtractor {
    List<String> extractPages(final InputStream pdfFileStream) throws IOException;

    static EstimateStreamExtractor inputStreamPdfExtractor() {
        return new EstimatePdfStreamExtractor();
    }

}

class EstimatePdfStreamExtractor implements EstimateStreamExtractor {

    @Override
    public List<String> extractPages(final InputStream pdfFileStream) {
        try {
            final PdfReader reader = new PdfReader(pdfFileStream);
            final List<String> pages = new ArrayList<>();
            for (int pageNo = 1; pageNo <= reader.getNumberOfPages(); pageNo++) {
                String textFromPage = PdfTextExtractor.getTextFromPage(reader, pageNo).toLowerCase();
                pages.add(textFromPage);
            }
            return pages;
        } catch (IOException e) {
            throw new RuntimeException(new ReaderException(e, "Couldn't extract PdfInputStream to pages"));
        }
    }

}
