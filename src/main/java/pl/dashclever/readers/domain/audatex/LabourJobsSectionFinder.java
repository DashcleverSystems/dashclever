package pl.dashclever.readers.domain.audatex;

import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;

import static pl.dashclever.readers.domain.audatex.SectionEndLineChecker.isLineSectionSeparationLine;

@RequiredArgsConstructor
class LabourJobsSectionFinder implements SectionFinder {

    private final List<String> estimatePages;
    @Override
    public List<String> findSectionLines() {
        var lines = wholeEstimateString().split("\n");
        var tasksSectionLines = new LinkedList<String>();
        var inTasksSection = false;
        var lineNo = 0;
        while (!inTasksSection) {
            if (lines[lineNo].toLowerCase().contains("prace naprawcze")) inTasksSection = true;
            lineNo++;
        }

        while (inTasksSection && lineNo < lines.length) {
            if (isLineSectionSeparationLine(lines[lineNo]))
                inTasksSection = false;
            else
                tasksSectionLines.add(lines[lineNo] + "\n");
            lineNo++;
        }

        return tasksSectionLines;
    }

    private String wholeEstimateString() {
        var wholeEstimate = new StringBuilder();
        for(var page : this.estimatePages) {
            wholeEstimate.append(page);
        }
        return wholeEstimate.toString();
    }
}
