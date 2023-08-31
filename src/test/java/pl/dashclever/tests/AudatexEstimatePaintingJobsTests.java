package pl.dashclever.tests;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.dashclever.readers.domain.EstimateReader;
import pl.dashclever.readers.domain.audatex.AudatexReader;

import java.util.List;

public class AudatexEstimatePaintingJobsTests
{
    //THEN_WHEN_GIVE
    @Test
    public void ReturnsProperPaintingsTasks_AfterEstimateRead()
    {
        //GIVEN
        final String estimate =
            """
            ----------------------------------------------------------------------------
            L A K I E R O W A N I E (według  A  Z  T)
            KOD CZĘŚCI                 OPERACJA   - LAKIER WODNY                     JC
                                                  - 2-WARSTW MINERAL
            0741          ŚCIANA BOCZNA PRZ L  LAK NOWA CZĘŚĆ ST 1                   11
            0283          ZDERZAK PRZ          LAK.NOW.CZ.ST.1 K1R                   23
            0313          DRZWI P L B/RAMY OKN LAK. WIERZCHNIE ST 2                   6
                                    company - name
                                    01-0001 CITY
                                 st. sezam street 11
                                tel./fax 123/1231234
                                   mobile 111 222 333
                                e-mail: some@codomain.xd
                                        www.coname.xd
            KOSZTY ROBOCIZNY LAKIERNICZEJ                JC        OPER.
                CENA WSPÓŁCZYNNIK           160.00 PLN/RBG
            """;
        EstimateReader audatexReader = new AudatexReader(Lists.newArrayList(estimate));
        //WHEN
        var paintingTasks = audatexReader.FindVarnishingJobs();
        //THEN
        Assertions.assertEquals(3, paintingTasks.size());
        Assertions.assertTrue(
                paintingTasks.stream()
                        .anyMatch(task ->
                        DoesTaskNameContains(task, Lists.newArrayList("ŚCIANA", "BOCZNA", "PRZ", "L", "LAK", "NOWA", "CZĘŚĆ", "ST", "1")))
        );
        Assertions.assertTrue(
                paintingTasks.stream().anyMatch(task ->
                DoesTaskNameContains(task, Lists.newArrayList("ZDERZAK", "PRZ", "LAK", "NOW", "CZ", "ST", "1", "K", "R" ))));
        Assertions.assertTrue(paintingTasks.stream().anyMatch(task ->
                DoesTaskNameContains(task, Lists.newArrayList("DRZWI", "P", "L", "B", "RAMY", "OKN", "LAK", "WIERZCH", "ST", "2" ))));
    }

    @Test
    public void ReturnsTasksContainingProperManMinutesAmount_AfterEstimateRead()
    {
        //GIVEN
        final String estimate =
                """
                ----------------------------------------------------------------------------
                L A K I E R O W A N I E (według  A  Z  T)
                KOD CZĘŚCI                 OPERACJA   - LAKIER WODNY                     JC
                                                      - 2-WARSTW MINERAL
                0283          ZDERZAK PRZ            ODNOWA LAKIERU                      23
                KOSZTY ROBOCIZNY LAKIERNICZEJ                JC        OPER.
                CENA  WSPÓŁCZYNNIK           160.00 PLN/RBG
                """;
        EstimateReader audatexReader = new AudatexReader(Lists.newArrayList(estimate));
        //WHEN
        var paintingTasks = audatexReader.FindVarnishingJobs();
        //THEN
        Assertions.assertTrue(
                paintingTasks.stream().anyMatch(task -> DoesTaskNameContains(task,
                        Lists.newArrayList("ZDERZAK", "PRZ", "ODNOWA", "LAKIERU")
                ))
        );
    }

    @Test
    public void ReturnsProperTaskWorthDenomination_AfterEstimateRead()
    {
        //GIVEN
        final String estimate =
        """
            ----------------------------------------------------------------------------
        L A K I E R O W A N I E (według  A  Z  T)
        KOD CZĘŚCI                 OPERACJA   - LAKIER WODNY                     JC
        - 2-WARSTW MINERAL
        0283          ZDERZAK PRZ                                                23
        KOSZTY ROBOCIZNY LAKIERNICZEJ                JC        OPER.
            CENA
        WSPÓŁCZYNNIK           160.00 PLN/RBG
        """;
        EstimateReader audatexReader = new AudatexReader(Lists.newArrayList(estimate));
        //WHEN
        var paintingTasks = audatexReader.FindVarnishingJobs().toArray(new EstimateReader.Job[0]);
        //THEN
        Assertions.assertEquals((36800), paintingTasks[0].worth().denomination());
        Assertions.assertEquals(EstimateReader.Currency.PLN, paintingTasks[0].worth().currency());
    }

    @Test
    public void ReturnsProperTaskWorthCurrency_AfterEstimateRead()
    {
        //GIVEN
        final String estimatePln =
            """
            ----------------------------------------------------------------------------
            L A K I E R O W A N I E (według  A  Z  T)
            KOD CZĘŚCI                 OPERACJA   - LAKIER WODNY                     JC
                                                  - 2-WARSTW MINERAL
            0283          ZDERZAK PRZ                                                23
            KOSZTY ROBOCIZNY LAKIERNICZEJ                JC        OPER.
            CENA    WSPÓŁCZYNNIK           160.00 PLN/RBG
            """;
        final String estimateEur =
            """
            ----------------------------------------------------------------------------
            L A K I E R O W A N I E (według  A  Z  T)
            KOD CZĘŚCI                 OPERACJA   - LAKIER WODNY                     JC
                                                  - 2-WARSTW MINERAL
            0283          ZDERZAK PRZ                                                23
            KOSZTY ROBOCIZNY LAKIERNICZEJ                JC        OPER.
            CENA  WSPÓŁCZYNNIK           160.00 EUR/RBG
            """;
        EstimateReader audatexReaderPLn = new AudatexReader(Lists.newArrayList(estimatePln));
        EstimateReader audatexReaderEur = new AudatexReader(Lists.newArrayList(estimateEur));
        //WHEN
        var paintingTasksPln = audatexReaderPLn.FindVarnishingJobs().toArray(new EstimateReader.Job[0]);
        var paintingTasksEur = audatexReaderEur.FindVarnishingJobs().toArray(new EstimateReader.Job[0]);
        //THEN
        Assertions.assertTrue(
                paintingTasksEur[0].worth().denomination() == 36800
                        && paintingTasksEur[0].worth().currency().equals(EstimateReader.Currency.EUR)
        );
        Assertions.assertTrue(paintingTasksPln[0].worth().denomination() == 36800
                && paintingTasksPln[0].worth().currency().equals(EstimateReader.Currency.PLN));
    }



    private static boolean DoesTaskNameContains(EstimateReader.Job task, List<String> list)
    {
        for (final var s : list)
        {
            if (!task.name().toLowerCase().contains(s.toLowerCase()))
                return false;
        }
        return true;
    }
}
