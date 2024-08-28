package subscribers.clearbunyang.domain.property.controller;


import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import subscribers.clearbunyang.domain.property.model.MyPropertiesResponseDTO;
import subscribers.clearbunyang.domain.property.model.MyPropertyCardDTO;
import subscribers.clearbunyang.domain.property.model.MyPropertyTableDTO;

@RestController
@RequestMapping("/api/mock/admin/my-properties")
public class PropertyMockController {
    @GetMapping("/card")
    public MyPropertiesResponseDTO<MyPropertyCardDTO> getCards(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size) {

        List<MyPropertyCardDTO> cards =
                Arrays.asList(
                        MyPropertyCardDTO.builder()
                                .imageUrl(
                                        "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/965/91cad0250f87de10f41a8f14170d9242_res.jpeg")
                                .id(13L)
                                .name("계양 학마을서원")
                                .address("인천시 계양구")
                                .createdAt("2024-07-20")
                                .endDate("2024-07-29")
                                .build(),
                        MyPropertyCardDTO.builder()
                                .imageUrl(
                                        "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/965/91cad0250f87de10f41a8f14170d9242_res.jpeg")
                                .id(13L)
                                .name("계양 학마을서원")
                                .address("인천시 계양구")
                                .createdAt("2024-07-21")
                                .endDate("2024-10-30")
                                .build(),
                        MyPropertyCardDTO.builder()
                                .imageUrl(
                                        "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/965/91cad0250f87de10f41a8f14170d9242_res.jpeg")
                                .id(13L)
                                .name("계양 학마을서원")
                                .address("인천시 계양구")
                                .createdAt("2024-07-21")
                                .endDate("2024-10-30")
                                .build(),
                        MyPropertyCardDTO.builder()
                                .imageUrl(
                                        "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/965/91cad0250f87de10f41a8f14170d9242_res.jpeg")
                                .id(13L)
                                .name("계양 학마을서원")
                                .address("인천시 계양구")
                                .createdAt("2024-07-21")
                                .endDate("2024-10-30")
                                .build());
        return MyPropertiesResponseDTO.<MyPropertyCardDTO>builder()
                .allProperties(5)
                .properties(cards)
                .build();
    }

    @GetMapping("/table")
    public MyPropertiesResponseDTO<MyPropertyTableDTO> getTable(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        List<MyPropertyTableDTO> tables =
                Arrays.asList(
                        MyPropertyTableDTO.builder()
                                .id(13L)
                                .name("계양 학마을서원")
                                .createdAt("2024-07-20")
                                .endDate("2024-07-29")
                                .totalNumber(342)
                                .pending(3)
                                .build(),
                        MyPropertyTableDTO.builder()
                                .id(13L)
                                .name("계양 학마을서원")
                                .createdAt("2024-07-20")
                                .endDate("2024-10-29")
                                .totalNumber(342)
                                .pending(3)
                                .build(),
                        MyPropertyTableDTO.builder()
                                .id(13L)
                                .name("계양 학마을서원")
                                .createdAt("2024-07-20")
                                .endDate("2024-10-29")
                                .totalNumber(342)
                                .pending(3)
                                .build(),
                        MyPropertyTableDTO.builder()
                                .id(13L)
                                .name("계양 학마을서원")
                                .createdAt("2024-07-20")
                                .endDate("2024-10-29")
                                .totalNumber(342)
                                .pending(3)
                                .build(),
                        MyPropertyTableDTO.builder()
                                .id(13L)
                                .name("계양 학마을서원")
                                .createdAt("2024-07-20")
                                .endDate("2024-10-29")
                                .totalNumber(342)
                                .pending(3)
                                .build());
        return MyPropertiesResponseDTO.<MyPropertyTableDTO>builder()
                .allProperties(6)
                .properties(tables)
                .build();
    }
}
