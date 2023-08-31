package pl.dashclever.readers.domain.audatex;

import org.apache.commons.lang3.StringUtils;

class SectionEndLineChecker {

    static boolean isLineSectionSeparationLine(String line) {
        if (line.isBlank())
            return false;
        return StringUtils.containsOnly(line, "-");
    }
}
