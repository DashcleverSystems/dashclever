package pl.dashclever.tests;

import org.junit.jupiter.api.Test;
import pl.dashclever.readers.domain.EstimateReader;
import pl.dashclever.readers.domain.EstimateReaderFactory;
import pl.dashclever.readers.domain.ReaderException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.dashclever.readers.domain.EstimateReader.Currency.PLN;

public class AudatexEstimateReadingLabourJobsTests {

    @Test
    public void ReturnsProperLabourJobsAmount_AfterReadEstimate() throws IOException, ReaderException {
        //GIVEN
        EstimateReader audatexReader = new EstimateReaderFactory
                (ToInputStreamConverter.fromString(MockPartialEstimates.PartialEstimateWithLabourJobsOnly))
                .withCustomStreamExtractor(new MockEstimateStringStreamExtractor());
        //WHEN
        var labourJobs = audatexReader.findLabourJobs();
        //THEN
        assertEquals(8, labourJobs.size());
    }

    @Test
    public void ReturnsLabourJobsWithProperDescription_AfterEstimateWithMultilineLabourJobsDescriptionIsRead() throws IOException, ReaderException {
        //GIVEN
        final String estimate = """
        r o b o c i z n a  baza czasowa     12 jc=1 rbg  cena/kl  1 =160.00  pln/rbg
                                                         cena/kl  2 =160.00  pln/rbg
                                                         cena/kl  3 =160.00  pln/rbg
        nr poz. pracy / prace naprawcze pojedyncze scalone       kl     jc    oper.
        kod cz                                                                cena
        51 11 592    nakladka zderzaka przedniego wybud/wbud     3       6    80.00
                     (zderzak przedni wybudowany)
        ----------------------------------------------------------------------------""";
        EstimateReader audatexReader = new EstimateReaderFactory
                (ToInputStreamConverter.fromString(estimate))
                .withCustomStreamExtractor(new MockEstimateStringStreamExtractor());
        //WHEN
        var labourJobs = audatexReader.findLabourJobs();
        //THEN
        assertEquals(1, labourJobs.size());
        assertTrue(labourJobs.stream().anyMatch(job -> {
                    String jobNameLowerCase = job.name().toLowerCase();
                    return jobNameLowerCase.contains("nakladka zderzaka przedniego wybud/wbud")
                    && jobNameLowerCase.contains("(zderzak przedni wybudowany)");
                })
        );
    }

    @Test
    public void ReturnsLabourJobsWithProperManHour_AfterEstimateIsRead() throws IOException, ReaderException {
        //GIVEN
        final String estimate = """
        r o b o c i z n a  baza czasowa     12 jc=1 rbg  cena/kl  1 =160.00  pln/rbg
        cena/kl  2 =160.00  pln/rbg
        cena/kl  3 =160.00  pln/rbg
        nr poz. pracy / prace naprawcze pojedyncze scalone       kl     jc    oper.
        kod cz                                                                cena
        51 11 592    nakladka zderzaka przedniego wybud/wbud     3       6    80.00
        (zderzak przedni wybudowany)
        ----------------------------------------------------------------------------""";
        EstimateReader audatexReader = new EstimateReaderFactory(ToInputStreamConverter.fromString(estimate))
                .withCustomStreamExtractor(new MockEstimateStringStreamExtractor());
        //WHEN
        var tasks = audatexReader.findLabourJobs();
        //THEN
        assertEquals(1, tasks.size());
        assertTrue(tasks.stream().allMatch(task -> task.manMinutes() == 30));
    }

    @Test
    public void ReturnsLabourJobsWithProperWorth_AfterEstimateWithPlnCurrencyIsRead() throws IOException, ReaderException {
        //GIVEN
        final String estimate = """
        r o b o c i z n a  baza czasowa     12 jc=1 rbg  cena/kl  1 =160.00  pln/rbg
        cena/kl  2 =160.00  pln/rbg
        cena/kl  3 =160.00  pln/rbg
        nr poz. pracy / prace naprawcze pojedyncze scalone       kl     jc    oper.
        kod cz                                                                cena
        51 11 592    nakladka zderzaka przedniego wybud/wbud     3       6    80.00
                       (zderzak przedni wybudowany)
        ----------------------------------------------------------------------------""";
        EstimateReader audatexReader = new EstimateReaderFactory(ToInputStreamConverter.fromString(estimate))
                .withCustomStreamExtractor(new MockEstimateStringStreamExtractor());
        //WHEN
        var jobs = audatexReader.findLabourJobs();
        //THEN
        assertEquals(1, jobs.size());
        assertTrue(jobs.stream().allMatch(job -> job.worth().denomination() == 8000 &&
                job.worth().currency().equals(EstimateReader.Currency.PLN)));
    }

    @Test
    public void ReturnsLabourJobsWithProperWorth_AfterEstimateWithEuroCurrencyIsRead() throws IOException, ReaderException {
        //GIVEN
        final String estimate = """
        r o b o c i z n a  baza czasowa     12 jc=1 rbg  cena/kl  1 =160.00  eur/rbg
        cena/kl  2 =160.00  eur/rbg
        cena/kl  3 =160.00  eur/rbg
        nr poz. pracy / prace naprawcze pojedyncze scalone       kl     jc    oper.
        kod cz                                                                cena
        51 11 592    nakladka zderzaka przedniego wybud/wbud     3       6    19.87
                      (zderzak przedni wybudowany)
        ----------------------------------------------------------------------------
        """;
        EstimateReader audatexReader = new EstimateReaderFactory(ToInputStreamConverter.fromString(estimate))
                .withCustomStreamExtractor(new MockEstimateStringStreamExtractor());
        //WHEN
        var jobs = audatexReader.findLabourJobs();
        //THEN
        assertEquals(1, jobs.size());
        assertTrue(jobs.stream().allMatch(job -> job.worth().denomination() == 1987
                && job.worth().currency().equals(EstimateReader.Currency.EUR)));
    }

    @Test
    public void ReturnsProperLabourJobs_AfterEstimateWithMultilineLabourJobsDescriptionWithRandomDashesLineIsRead() throws IOException, ReaderException {
        // GIVEN
        final String estimate = """
                r o b o c i z n a  baza czasowa     12 jc=1 rbg  cena/kl  1 =160.00  pln/rbg
                                                                 cena/kl  2 =160.00  pln/rbg
                                                                 cena/kl  3 =160.00  pln/rbg
                nr poz. pracy / prace naprawcze pojedyncze scalone               jc    oper.
                kod cz                                                                  cena
                1201        PODLUZNICA P L NAPRAWA                                30*  80.00
                                   ---------------------------------------------------------
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
                        "PODLUZNICA P L NAPRAWA",
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
