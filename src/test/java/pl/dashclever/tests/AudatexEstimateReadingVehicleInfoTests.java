package pl.dashclever.tests;

import org.junit.jupiter.api.Test;
import pl.dashclever.readers.domain.EstimateReader;
import pl.dashclever.readers.domain.EstimateReaderFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AudatexEstimateReadingVehicleInfoTests {

    @Test
    public void ReturnsVehicleInfo_AfterEstimateIsRead() throws IOException {
        //GIVEN
        final String estimate = """
        DANE POJAZDU
        MARKA  :  RENAULT
        TYP/MODEL : CLIO IV GRANDTOUR / LIMITED
        RODZAJ SILNIKA/POJEMNOŚĆ/MOC : Benzynowy / 898 CCM / 66 KW
        MASA POJ./MASA CAŁKOWITA POJ.: 1182 / 1639
        NR REJESTRACYJNY/NR  IDENT. : XXX XX56 / XX11XX11X11111111
        LAKIEROWANIE  (ODCIEŃ/RODZAJ)   : CZERWONA PERŁ. 3W. NNP
        """;
        EstimateReader audatexReader = new EstimateReaderFactory(ToInputStreamConverter.fromString(estimate))
                .withCustomStreamExtractor(new MockEstimateStringStreamExtractor());
        //WHEN
        var vehicleInfo = audatexReader.FindVehicleInfo();
        //THEN
        assertEquals("RENAULT", vehicleInfo.brand());
        assertTrue(vehicleInfo.model().contains("CLIO IV GRANDTOUR"));
        assertEquals("XXXXX56", vehicleInfo.registration());
    }

    @Test
    public void ReturnsProperVehicleInfo_AfterMockBmwEstimateIsRead() throws IOException {
        EstimateReader audatexReader = new EstimateReaderFactory(ToInputStreamConverter.fromString(MockWholeEstimate.WholeEstimate))
                .withCustomStreamExtractor(new MockEstimateStringStreamExtractor());
        //WHEN
        var vehicleInfo = audatexReader.FindVehicleInfo();
        //THEN
        assertEquals("bmw", vehicleInfo.brand().toLowerCase());
        assertTrue(vehicleInfo.model().toLowerCase().contains("5 szereg f10 / 525 d"));
        assertEquals("xxx11111", vehicleInfo.registration().toLowerCase());
    }
}
