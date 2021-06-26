package nextstep.subway.line.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;


@Getter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_section_station_info", columnNames={"line_id", "up_station_id", "down_station_id"}))
public class Section extends BaseEntity {

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_section_to_line"))
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id", foreignKey = @ForeignKey(name = "fk_section_to_up_station"))
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id", foreignKey = @ForeignKey(name = "fk_section_to_down_station"))
    private Station downStation;

    private int distance;

    @Builder
    public Section(final Long id, final Station upStation, final Station downStation, final int distance) {
        this.id = id;
        this.registerStation(upStation, downStation, distance);
    }

    public void registerStation(final Station upStation, final Station downStation, final int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void registerLine(Line line) {
        Optional.ofNullable(line).ifPresent(it -> {
            this.line = line;
            it.addSection(this);
        });
    }

    protected boolean isBefore(Section section) {
        return Objects.equals(downStation, section.getUpStation());
    }

    protected boolean isAfter(Section section) {
        return Objects.equals(upStation, section.getDownStation());
    }

    protected boolean contains(Station station) {
        return Objects.equals(upStation, station) || Objects.equals(downStation, station);
    }

    protected boolean hasSameUpStation(Section section) {
        return Objects.equals(upStation, section.getUpStation());
    }

    protected boolean hasSameDownStation(Section section) {
        return Objects.equals(downStation, section.getDownStation());
    }

    protected void updateUpStation(final Section section) {
        updateDistance(section);

        this.upStation = section.downStation;
    }

    protected void updateDownStation(final Section section) {
        updateDistance(section);

        this.downStation = section.upStation;
    }

    private void updateDistance(final Section section) {
        if (isUpdateDistance(section)) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }

        this.distance = getIntervalDistance(section);
    }

    private boolean isUpdateDistance(final Section section) {
        int distance = getIntervalDistance(section);
        return distance < 1;
    }

    private int getIntervalDistance(final Section section) {
        return this.distance - section.getDistance();
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", upStation, downStation, distance);
    }
}
