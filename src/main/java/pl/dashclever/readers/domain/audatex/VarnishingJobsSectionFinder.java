package pl.dashclever.readers.domain.audatex;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static pl.dashclever.readers.domain.audatex.SectionEndLineChecker.isLineSectionSeparationLine;

@RequiredArgsConstructor
class VarnishingJobsSectionFinder implements SectionFinder {

    private final List<String> estimatePages;

    @Override
    public List<String> findSectionLines() {
        var wholeEstimate = new StringBuilder();
        for (final var page : this.estimatePages) {
            wholeEstimate.append(page);
        }
        var lines = wholeEstimate.toString().split("\\n");
        var paintingTasksSectionLines = new ArrayList<String>();
        var inPaintingsSection = false;
        var lineNo = 0;
        while (!inPaintingsSection && lineNo < lines.length) {
            var line = lines[lineNo].replaceAll("\\s+", "");
            if (line.toLowerCase().contains("LAKIEROWANIE".toLowerCase()) && line.toLowerCase().contains("azt")) {
                inPaintingsSection = true;
            }
            lineNo++;
        }

        while (inPaintingsSection && lineNo < lines.length) {
            var line = lines[lineNo];
            paintingTasksSectionLines.add(line);
            lineNo++;
            if (isLineSectionSeparationLine(line)) {
                inPaintingsSection = false;
            }
        }

        return paintingTasksSectionLines;
    }
}
