package study.unit;

import com.google.common.collect.Lists;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Disabled
@DisplayName("단위 테스트 - mockito의 MockitoExtension을 활용한 가짜 협력 객체 사용")
@ExtendWith(MockitoExtension.class)
public class MockitoExtensionTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private StationRepository stationRepository;

    @Test
    void findAllLines() {
        // given
        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(new Line()));
        LineService lineService = new LineService(lineRepository, sectionRepository, stationRepository);

        // when
        List<LineResponse> responses = lineService.findLines();

        // then
        assertThat(responses).hasSize(1);
    }
}
