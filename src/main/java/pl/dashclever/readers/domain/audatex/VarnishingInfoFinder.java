package pl.dashclever.readers.domain.audatex;

import pl.dashclever.readers.domain.EstimateReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class VarnishingInfoFinder {

    private final List<String> estimatePages;

    VarnishingInfoFinder(List<String> estimatePages) {
        this.estimatePages = estimatePages;
    }

    List<EstimateReader.Job> find() {
        var paintingTasksLines = findPaintingTasksLines();
        var manySpacesSeparatedTaskLineGroupingPattern =
            Pattern.compile("(\\s+(?<taskName>(([a-zA-Z.0-9ąćęłńóśźżŚĄĆĘŁŃÓŹŻ\\\\/]+\\s)+)\\s+(([a-zA-Z.0-9ąćęłńóśźżŚĄĆĘŁŃÓŹŻ\\/-]+\\s)+)))\\s+(?<taskTimeUnits>\\d+)\\*?$");
        var noSpacesSeparatedTaskLineGroupingPattern =
            Pattern.compile("\\s+(?<taskName>(([a-zA-Z.0-9ąćęłńóśźżŚĄĆĘŁŃÓŹŻ\\\\/-]+\\s)+))\\s+(?<taskTimeUnits>\\d+)\\*?$");
        var tasks = new LinkedList<EstimateReader.Job>();
        for (var line : paintingTasksLines) {
            var multiSpacesSeparatedLineMatch = manySpacesSeparatedTaskLineGroupingPattern.matcher(line);
            var noSpacesSeparatedMatch = noSpacesSeparatedTaskLineGroupingPattern.matcher(line);
            if (multiSpacesSeparatedLineMatch.matches())
                tasks.add(MatchToTask(multiSpacesSeparatedLineMatch));
            else if (noSpacesSeparatedMatch.matches())
                tasks.add(MatchToTask(noSpacesSeparatedMatch));
        }

        return tasks;
    }

    private EstimateReader.Job MatchToTask(Matcher taskLineMatcher) {
        var taskName = taskLineMatcher.group("taskName");
        var taskTimeUnits = Integer.parseInt(taskLineMatcher.group("taskTimeUnits"));
        var timeUnitsInManHour = findTimeUnitsInManHour();
        var taskManMinutes = taskTimeUnits / timeUnitsInManHour * 60;
        var manHourWorth = findManHourWorth();
        var taskWorth = taskTimeUnits / timeUnitsInManHour * manHourWorth.denomination();
        taskName = taskName.replaceAll("\\s{2,}", " ");
        return new EstimateReader.Job(taskName, (int) taskManMinutes, new EstimateReader.Money((long) taskWorth, manHourWorth.currency()));
    }

    private EstimateReader.Money findManHourWorth() {
        var paintingsSectionLines = new VarnishingJobsSectionFinder(this.estimatePages).findSectionLines();
        var manHourWorthDefinition = paintingsSectionLines.stream()
            .filter(line -> line.toLowerCase().contains("/rbg") && line.toLowerCase().contains("współczynnik"))
            .findFirst()
            .orElse("");
        Pattern pattern = Pattern.compile("(\\d+.\\d+)\\s+([A-Za-z]+)[\\\\/][Rr][Bb][Gg]");
        var worthMatch = pattern.matcher(manHourWorthDefinition);
        if (!worthMatch.find())
            return new EstimateReader.Money(0, EstimateReader.Currency.PLN);
        var moneyAmount = worthMatch.group(1);
        var currencyCode = worthMatch.group(2);
        var currency = EstimateReader.Currency.valueOf(currencyCode.toUpperCase());
        int denominatedMoneyAmount = (int) Float.parseFloat(moneyAmount) * 100;
        return new EstimateReader.Money(denominatedMoneyAmount, currency);
    }

    private float findTimeUnitsInManHour() {
        return 10;
    }

    private List<String> findPaintingTasksLines() {
        var paintingTasksSection = new VarnishingJobsSectionFinder(this.estimatePages).findSectionLines();
        Pattern manySpacesSeparatedTaskPattern =
            Pattern.compile("(?<grp>(\\s+([a-zA-Z.0-9ąćęłńóśźżŚĄĆĘŁŃÓŹŻ\\\\/-]+\\s)+)\\s+(([a-zA-Z.0-9ąćęłńóśźżŚĄĆĘŁŃÓŹŻ\\\\/-]+\\s)+)\\s+\\d+\\*?$)");
        Pattern noSpacesSeparatedTaskLine =
            Pattern.compile("(?<grp>\\s+(([a-zA-Z.0-9ąćęłńóśźżŚĄĆĘŁŃÓŹŻ\\\\/-]+\\s)+)\\s+\\d+\\*?$)");
        var tasksLines = new LinkedList<String>();
        for (var line : paintingTasksSection) {
            var manySpacesMatcher = manySpacesSeparatedTaskPattern.matcher(line);
            var noSpacesMatcher = noSpacesSeparatedTaskLine.matcher(line);
            if (!(line.toLowerCase().contains("czas") && line.toLowerCase().contains("lakierowania"))) {
                if (manySpacesMatcher.find())
                    tasksLines.add(manySpacesMatcher.group("grp"));
                else if (noSpacesMatcher.find())
                    tasksLines.add(noSpacesMatcher.group("grp"));
            }
        }

        return tasksLines;
    }

    List<String> findVarnishingPaintInfo() {
        final List<String> paintingsSectionLines = new VarnishingJobsSectionFinder(this.estimatePages).findSectionLines();
        Pattern manySpacesSeparatedTaskPattern =
            Pattern.compile("(?<grp>(\\s+([a-zA-Z.0-9ąćęłńóśźżŚĄĆĘŁŃÓŹŻ\\\\/]+\\s)+)\\s+(([a-zA-Z.0-9ąćęłńóśźżŚĄĆĘŁŃÓŹŻ\\\\/]+\\s)+)\\s+\\d+\\*?$)");
        Pattern noSpacesSeparatedTaskLine =
            Pattern.compile("(?<grp>\\s+(([a-zA-Z.0-9ąćęłńóśźżŚĄĆĘŁŃÓŹŻ\\\\/]+\\s)+)\\s+\\d+\\*?$)");
        List<String> additionalInfoSection = new LinkedList<>();
        for (String sectionLine : paintingsSectionLines) {
            if (manySpacesSeparatedTaskPattern.matcher(sectionLine).find() ||
                noSpacesSeparatedTaskLine.matcher(sectionLine).find())
                break;
            additionalInfoSection.add(sectionLine.replace("jc", ""));
        }
        var additionalInfoLines = new ArrayList<String>();
        for (final var sectionLine : additionalInfoSection) {
            Pattern pattern = Pattern.compile("\\s+-\\s+([-\\w\\\\/\\s]+)");
            Matcher matcher = pattern.matcher(sectionLine);
            if (matcher.find())
                additionalInfoLines.add(matcher.group(1).trim());
        }

        return additionalInfoLines;
    }

}
