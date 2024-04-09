package pl.dashclever.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import pl.dashclever.readers.domain.EstimateReader;
import pl.dashclever.readers.domain.EstimateReaderFactory;

public class AudatexEstimateReadingCustomerInfoTests {

    @Test
    public void shouldReturnCustomerName() throws IOException {
        //GIVEN
        final String estimate = """
            SYGNATURA AKT/RODZAJ SZKODY : PL2021122881131 / OC
            WŁAŚCICIEL POJAZDU : Patrick Star-Smith
                               : Sezame 13B/4
                               : 11-111 Ocean
            """;
        EstimateReader audatexReader = new EstimateReaderFactory(ToInputStreamConverter.fromString(estimate))
            .withCustomStreamExtractor(new MockEstimateStringStreamExtractor());

        //WHEN
        var customerInfo = audatexReader.findCustomerInfo();

        //THEN
        assertEquals(customerInfo.customerName(), "Patrick Star-Smith");
    }
}
