package pl.dashclever.tests;

import org.junit.jupiter.api.Test;
import pl.dashclever.readers.domain.EstimateReader;
import pl.dashclever.readers.domain.EstimateReaderFactory;
import pl.dashclever.readers.domain.ReaderException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.dashclever.readers.domain.EstimateReader.Currency.PLN;

public class AudatexEstimateReadingLabourJobsOnSplitPagesTests {

    @Test
    public void ReturnsProperLabourJobs_AfterEstimateReadWithLabourJobsSplitOntoMultiplePages() throws IOException, ReaderException {
        // GIVEN
        final String estimate = """
                r o b o c i z n a  baza czasowa     12 jc=1 rbg  cena/kl  1 =160.00  pln/rbg
                                                                 cena/kl  2 =160.00  pln/rbg
                                                                 cena/kl  3 =160.00  pln/rbg
                nr poz. pracy / prace naprawcze pojedyncze scalone               jc    oper.
                kod cz                                                                  cena
                1201        PODLUZNICA P L NAPRAWA                                30*  80.00
                                   ---------------------------------------------------------
                                   SYSTEM AUDATEX                                   STRONA 2
                                    AUTO-DUBNICKI

                                    75-811 KOSZALIN
                                   UL. POŁCZYŃSKA 117
                                  TEL./FAX 094/3410244
                                   MOBILE 602 703 598
                                 e-mail: motrio@dubnicki.pl
                                    www.dubnicki.pl

                            OBEJMUJE: KONSERWERACJE
                BRAK NR     CZESC NADWOZIA KONSERW PRZESTRZ ZAMKN                  2   28.00
                ----------------------------------------------------------------------------
                """;
        EstimateReader audatexReader = new EstimateReaderFactory
                (ToInputStreamConverter.fromString(estimate))
                .withCustomStreamExtractor(new MockEstimateStringStreamExtractor());
        // WHEN
        var labourJobs = audatexReader.findLabourJobs();

        // THEN
        assertEquals(2, labourJobs.size());
        assertThat(labourJobs).contains(
                new EstimateReader.Job(
                        "PODLUZNICA P L NAPRAWA OBEJMUJE: KONSERWERACJE",
                        150,
                        new EstimateReader.Money(8000, PLN))
        );
        assertThat(labourJobs).contains(
                new EstimateReader.Job(
                        "CZESC NADWOZIA KONSERW PRZESTRZ ZAMKN",
                        10,
                        new EstimateReader.Money(2800, PLN)
                )
        );
    }
}
