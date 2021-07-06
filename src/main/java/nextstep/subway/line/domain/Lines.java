package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> lines;

    public Lines(final List<Line> lines) {
        this.lines = lines;
    }

    public List<Station> getStationAll() {
        List<Station> stations = lines.stream()
            .flatMap(it -> it.getSections().unSortedStations().stream())
            .distinct()
            .collect(Collectors.toList());

        return Collections.unmodifiableList(stations);
    }

    public List<Section> getSectionsAll() {
        List<Section> sections = lines.stream()
            .flatMap(it -> it.getSections().sortedSections().stream())
            .distinct()
            .collect(Collectors.toList());

        return Collections.unmodifiableList(sections);
    }

    public int getMostExpensiveFee(List<Station> stations) {
        Fee fee = stations.stream()
                .flatMap(station -> findLineAllByStation(station).stream())
                .map(Line::getFee)
                .sorted().findFirst().orElse(new Fee());

        return fee.getFee();
    }

    public int size() {
        return lines.size();
    }

    private List<Line> findLineAllByStation(Station station) {
        return lines.stream()
                .filter(line -> line.containsByStation(station))
                .collect(Collectors.toList());
    }

}
