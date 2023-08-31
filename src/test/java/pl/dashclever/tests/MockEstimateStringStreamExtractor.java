package pl.dashclever.tests;

import pl.dashclever.readers.domain.EstimateStreamExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class MockEstimateStringStreamExtractor implements EstimateStreamExtractor {

        @Override
        public List<String> extractPages(final InputStream pdfFileStream) throws IOException {
                BufferedReader reader = new BufferedReader(new InputStreamReader(pdfFileStream));
                List<String> pages = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                while(reader.ready()) {
                        String line = reader.readLine();
                        sb.append(line).append("\n");
                        if (line.toLowerCase().contains("strona")) {
                                pages.add(sb.toString());
                                sb = new StringBuilder();
                        }
                }
                if (!sb.isEmpty())
                        pages.add(sb.toString());
                return pages;
        }
}
