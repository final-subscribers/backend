package subscribers.clearbunyang.domain.property.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.file.entity.File;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;
import subscribers.clearbunyang.domain.file.repository.FileRepository;
import subscribers.clearbunyang.domain.property.entity.Keyword;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordName;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.property.model.response.HomePropertiesResponse;
import subscribers.clearbunyang.domain.property.model.response.HomeResponse;
import subscribers.clearbunyang.domain.property.repository.KeywordRepository;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.domain.user.entity.enums.AdminState;
import subscribers.clearbunyang.domain.user.repository.AdminRepository;
import subscribers.clearbunyang.global.model.PagedDto;

@SpringBootTest
@DisplayName("HomeService-통합 테스트")
@Transactional
public class HomeServiceTest {

    @Autowired private HomeService homeService;
    @Autowired private PropertyRepository propertyRepository;
    @Autowired private KeywordRepository keywordRepository;
    @Autowired private AdminRepository adminRepository;
    @Autowired private FileRepository fileRepository;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin =
                Admin.builder()
                        .name("Admin Name")
                        .email("admin@example.com")
                        .password("securepassword")
                        .phoneNumber("010-1234-5678")
                        .companyName("Company Name")
                        .address("Address Line 1")
                        .business("Business Type")
                        .status(AdminState.ACCEPTED)
                        .build();
        admin = adminRepository.save(admin);

        for (int i = 0; i < 25; i++) {
            Property property =
                    Property.builder()
                            .imageUrl("https://example.com/image" + i + ".jpg")
                            .name("Property " + i)
                            .constructor("Constructor " + i)
                            .areaAddr("Area Addr " + i)
                            .modelHouseAddr("Model House Addr " + i)
                            .phoneNumber("123-456-7890")
                            .contactChannel("Contact Channel " + i)
                            .homePage("Home Page " + i)
                            .likeCount(10000 + i)
                            .startDate(LocalDate.now().minusDays(10))
                            .endDate(LocalDate.now().plusDays(10))
                            .propertyType(PropertyType.APARTMENT)
                            .salesType(SalesType.PRIVATE_SALE)
                            .totalNumber(100 + i)
                            .companyName("Company " + i)
                            .addrDo("Addr Do " + i)
                            .addrGu("Addr Gu " + i)
                            .addrDong("Addr Dong " + i)
                            .buildingName("Building Name " + i)
                            .price(1000000 + i * 1000)
                            .discountPrice(500000 + i * 500)
                            .discountPercent(10 + (i % 5))
                            .admin(admin)
                            .build();

            propertyRepository.save(property);

            for (int j = 0; j < 3; j++) {
                Keyword keyword =
                        Keyword.builder()
                                .name(KeywordName.values()[j % KeywordName.values().length])
                                .type(KeywordType.values()[j % KeywordType.values().length])
                                .jsonValue("{\"sampleKey\": \"sampleValue\"}")
                                .isSearchable(true)
                                .property(property)
                                .build();
                keywordRepository.save(keyword);
            }

            for (int k = 0; k < 3; k++) {
                File file =
                        File.builder()
                                .property(property)
                                .admin(admin)
                                .name("File " + k + " for Property " + i)
                                .link("https://example.com/file" + k + ".jpg")
                                .type(FileType.PROPERTY_IMAGE)
                                .build();
                fileRepository.save(file);
            }
        }
    }

    @Test
    @DisplayName("getHome 메서드 페이지네이션 테스트")
    void getHomePaginationTest() {
        int page = 0;
        int size = 5;

        PagedDto<HomeResponse> response = homeService.getHome(null, page);

        assertThat(response.getCurrentPage()).isEqualTo(page);
        assertThat(response.getPageSize()).isEqualTo(size);
        assertThat(response.getTotalPages()).isEqualTo(4);
        assertThat(response.getContents()).hasSize(1);

        HomeResponse homeResponse = response.getContents().get(0);
        List<HomePropertiesResponse> properties = homeResponse.getProperties();

        assertThat(properties).hasSize(size);
        assertThat(properties.get(0).getPropertyName()).startsWith("Property");
    }

    @Test
    @DisplayName("getHome 메서드 두 번째 페이지 테스트")
    void getHomeSecondPageTest() {
        int page = 1;
        int size = 5;

        PagedDto<HomeResponse> response = homeService.getHome(null, page);

        assertThat(response.getCurrentPage()).isEqualTo(page);
        assertThat(response.getPageSize()).isEqualTo(size);
        assertThat(response.getTotalPages()).isEqualTo(4);
        assertThat(response.getContents()).hasSize(1);

        HomeResponse homeResponse = response.getContents().get(0);
        List<HomePropertiesResponse> properties = homeResponse.getProperties();

        assertThat(properties).hasSize(size);
        assertThat(properties.get(0).getPropertyName()).startsWith("Property");
    }
}
