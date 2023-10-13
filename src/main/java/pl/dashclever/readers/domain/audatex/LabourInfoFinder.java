package pl.dashclever.readers.domain.audatex;

import com.google.common.collect.Sets;

import org.apache.commons.lang3.StringUtils;

import pl.dashclever.readers.domain.EstimateReader;
import pl.dashclever.readers.domain.ReaderException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LabourInfoFinder {

    private final List<String> estimatePages;

    LabourInfoFinder(List<String> estimatePages) {
        this.estimatePages = estimatePages;
    }

    List<EstimateReader.Job> find() throws ReaderException {
        float timeUnitsInManHour = findTimeUnitsAmountInManHour();
        EstimateReader.Currency tasksCurrency = findTasksCurrency();
        String[] tasksLines = findTasksDescriptionLines().toArray(new String[0]);
        var taskInfoGroupingRegex =
            Pattern.compile("(?<name>[A-Za-z0-9(ąćęłńóśźż]([A-Za-z/.ąćęłńóśźż()-\\\\\"]+ ){2,})\\s+\\d*\\*?\\s+(?<timeUnits>\\d+)\\*?\\s+(?<worth>\\d+.\\d+)\\*?$");

        var tasks = new LinkedList<EstimateReader.Job>();
        for (var i = 0; i < tasksLines.length; i++) {
            Matcher matcher = taskInfoGroupingRegex.matcher(tasksLines[i]);
            if (matcher.find()) {
                var taskName = matcher.group("name");
                float taskTimeInHours = Integer.parseInt(matcher.group("timeUnits")) / timeUnitsInManHour;
                float taskTimeInMinutes = 60 * taskTimeInHours;
                float taskWorth = Float.parseFloat(matcher.group("worth"));
                int j = i + 1;
                while (j < tasksLines.length) {
                    Matcher matcherr = taskInfoGroupingRegex.matcher(tasksLines[j]);
                    if (matcherr.find())
                        break;
                    else taskName += tasksLines[j].trim().replaceAll("\\s{2,}", "");
                    j++;
                }
                var task = new EstimateReader.Job(taskName.trim(), (int) taskTimeInMinutes, new EstimateReader.Money((long) (taskWorth * 100), tasksCurrency));
                tasks.add(task);
            }

        }
        return tasks;
    }

    private List<String> findTasksDescriptionLines() {
        List<String> tasksSectionLines = new LabourJobsSectionFinder(this.estimatePages).findSectionLines();
        List<String> taskLines = new LinkedList<>();
        for (int i = 0; i < tasksSectionLines.size(); i++) {
            Matcher taskDescriptionMatch = matchTaskDescriptionLine(tasksSectionLines.get(i));
            if (i > 0) {
                String previousLineNoSpacesLower = tasksSectionLines.get(i - 1).replaceAll(" ", "").toLowerCase();
                if (previousLineNoSpacesLower.contains("strona") && previousLineNoSpacesLower.contains("audatex"))
                    continue;
            }
            if (taskDescriptionMatch.find()) {
                taskLines.add(taskDescriptionMatch.group(1));
            } else if (isTaskDescriptionLine(tasksSectionLines.get(i))) {
                taskLines.add(tasksSectionLines.get(i));
            }
        }

        return taskLines;
    }

    private boolean isTaskDescriptionLine(String line) {
        var lineNoSpacesLow = line.replace(" ", "").toLowerCase();
        if (SectionEndLineChecker.isLineSectionSeparationLine(line))
            return false;
        else if (lineNoSpacesLow.contains("kod") && line.contains("cz"))
            return false;
        else if (lineNoSpacesLow.isBlank())
            return false;
        else if (lineNoSpacesLow.contains("system") && lineNoSpacesLow.contains("audatex"))
            return false;
        else if (lineNoSpacesLow.matches("strona\\d+$"))
            return false;
        else if (isContactInfoContentLine(line))
            return false;
        else if (lineNoSpacesLow.contains("kalkulacja") && lineNoSpacesLow.contains("naprawy"))
            return false;
        else if (lineNoSpacesLow.contains("cena/kl"))
            return false;
        else if (lineNoSpacesLow.contains("kl") && lineNoSpacesLow.contains("jc"))
            return false;
        else if (lineNoSpacesLow.contains("jc=") && lineNoSpacesLow.contains("cena="))
            return false;
        else if (lineNoSpacesLow.contains("jc") && lineNoSpacesLow.contains("oper.") && lineNoSpacesLow.contains("poz."))
            return false;
        else if (StringUtils.containsOnly(StringUtils.removeEnd(lineNoSpacesLow, "\n"), "-"))
            return false;
        else
            return true;
    }

    private boolean isContactInfoContentLine(String line) {
        final Set<Pattern> contactInfoLineRegexes = Sets.newHashSet(
            Pattern.compile("\\d\\d-\\d\\d\\d"),
            Pattern.compile("^ul[.]\\w+"),
            Pattern.compile("^tel[.]"),
            Pattern.compile("^mobile"),
            Pattern.compile("^e-mail:\\w+"),
            Pattern.compile("^www\\.\\w+\\.")
        );
        for (var pattern : contactInfoLineRegexes) {
            var lineLowerNoSpaces = line.replaceAll(" ", "").toLowerCase();
            if (pattern.matcher(lineLowerNoSpaces).find())
                return true;
        }
        return false;
    }

    private Matcher matchTaskDescriptionLine(String line) {
        Pattern regex = Pattern
            .compile("([A-Za-z0-9(ąćęłńóśźż]([A-Za-z/ą.ćęłńóśźż()-\\\\\"]+ ){2,}\\s+\\d*\\*?\\s+\\d+\\*?\\s+\\d+.\\d+\\*?$)");
        return regex.matcher(line);
    }

    private EstimateReader.Currency findTasksCurrency() throws ReaderException {
        var tasksSection = wholeEstimateString().split("\n");
        for (String line : tasksSection) {
            Pattern currencyGroupingRegex = Pattern.compile("\\s+([A-Za-z]+)[\\\\/]rbg\\s?");
            Matcher currencyGroupingRegexMatcher = currencyGroupingRegex.matcher(line);
            if (currencyGroupingRegexMatcher.find()) {
                var currencyCode = currencyGroupingRegexMatcher.group(1);
                try {
                    return EstimateReader.Currency.valueOf(currencyCode.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    throw new ReaderException(ex, "Unknown currency");
                }
            }
        }
        throw new ReaderException("Unknown currency");
    }

    private float findTimeUnitsAmountInManHour() throws ReaderException {
        var wholeEstimate = wholeEstimateString();
        String[] lines = wholeEstimate.split("\n");
        //jc = so-called time units in estimate; rgb = so-called man-hour in estimate
        var lineWithJcToRbg = Arrays.stream(lines).filter(line -> line.contains("jc="))
            .findFirst()
            .orElseThrow(() -> new ReaderException("Could not locate any line containing definition of how many time units (JC) is in one man hour (RBG)"));
        Pattern jcInRbgPattern = Pattern.compile("(?<jcAmount>\\d+)\\s+jc=(?<rgbAmount>\\d+)\\srbg");
        var timeUnitsInManHour = jcInRbgPattern.matcher(lineWithJcToRbg);
        if (!timeUnitsInManHour.find())
            throw new ReaderException("Could not group with regex time units in man hour in time units in man hour defining line");
        float timeUnits = (float) Integer.parseInt(timeUnitsInManHour.group("jcAmount"));
        float manHours = (float) Integer.parseInt(timeUnitsInManHour.group("rgbAmount"));
        return timeUnits / manHours;
    }

    private String wholeEstimateString() {
        var wholeEstimate = new StringBuilder();
        for (var page : this.estimatePages) {
            wholeEstimate.append(page);
        }
        return wholeEstimate.toString();
    }
}
