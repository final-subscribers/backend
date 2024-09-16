package subscribers.clearbunyang.domain.property.service;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subscribers.clearbunyang.domain.property.entity.Area;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.model.request.AreaRequestDTO;
import subscribers.clearbunyang.domain.property.repository.AreaRepository;

@RequiredArgsConstructor
@Service
public class AreaService {
    private final AreaRepository areaRepository;

    /**
     * 세대 면적을 저장하는 메소드
     *
     * @param areaRequestDTOS
     * @param property
     */
    public void saveAreas(List<AreaRequestDTO> areaRequestDTOS, Property property) {
        List<Area> areas =
                areaRequestDTOS.stream()
                        .map(areaDTO -> Area.toEntity(areaDTO, property))
                        .collect(Collectors.toList());

        areaRepository.saveAll(areas);
    }
}
