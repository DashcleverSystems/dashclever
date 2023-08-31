package pl.dashclever.tests;


import org.junit.jupiter.api.Test;
import pl.dashclever.readers.domain.EstimateReader;
import pl.dashclever.readers.domain.EstimateReaderFactory;
import pl.dashclever.readers.domain.ReaderException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AudatexEstimateReadingLabourJobsWithNoKlIndicatorTests {

    @Test
    public void ReturnsLabourJobsWithProperDescription_AfterEstimateWithMultilineLabourJobsDescriptionIsRead() throws IOException, ReaderException {
        //GIVEN
        final String estimate = """
        r o b o c i z n a  baza czasowa     12 jc=1 rbg  cena/kl  1 =160.00  pln/rbg
                                                         cena/kl  2 =160.00  pln/rbg
                                                         cena/kl  3 =160.00  pln/rbg
        nr poz. pracy / prace naprawcze pojedyncze scalone               jc    oper.
        kod cz                                                                 cena
        51 11 592    nakladka zderzaka przedniego wybud/wbud             6    80.00
                     obejmuje: wymiana 2 p cz. parkw.e
                     ( montaż/demontaż )
        ----------------------------------------------------------------------------""";
        EstimateReader audatexReader = new EstimateReaderFactory
                (ToInputStreamConverter.fromString(estimate))
                .withCustomStreamExtractor(new MockEstimateStringStreamExtractor());
        //WHEN
        var labourJobs = audatexReader.findLabourJobs();
        //THEN
        assertEquals(1, labourJobs.size());
        assertTrue(labourJobs.stream().anyMatch(job ->
                    job.name().contains("nakladka zderzaka przedniego wybud/wbud")
                            && job.name().contains("( montaż/demontaż )")
                            && job.name().contains("obejmuje: wymiana 2 p cz. parkw.")
                )
        );
    }
}
