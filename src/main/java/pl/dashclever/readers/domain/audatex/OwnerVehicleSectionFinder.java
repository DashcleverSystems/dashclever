package pl.dashclever.readers.domain.audatex;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
class OwnerVehicleSectionFinder implements SectionFinder {

    private final List<String> estimatePages;

    @Override
    public List<String> findSectionLines() {
        return Arrays.stream(estimatePages.get(0).split("\n")).toList();
    }
}
