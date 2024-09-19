package subscribers.clearbunyang.domain.property.service;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.property.dto.request.AreaRequest;
import subscribers.clearbunyang.domain.property.entity.Area;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.repository.AreaRepository;

@RequiredArgsConstructor
@Service
public class AreaService {
    private final AreaRepository areaRepository;

    /**
     * 세대 면적을 저장하는 메소드
     *
     * @param areaRequests
     * @param property
     */
    @Transactional
    public void saveAreas(List<AreaRequest> areaRequests, Property property) {
        List<Area> areas =
                areaRequests.stream()
                        .map(areaDTO -> Area.toEntity(areaDTO, property))
                        .collect(Collectors.toList());

        areaRepository.saveAll(areas);
    }
}
