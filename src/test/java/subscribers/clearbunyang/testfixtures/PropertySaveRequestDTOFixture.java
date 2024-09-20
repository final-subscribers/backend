package subscribers.clearbunyang.testfixtures;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import subscribers.clearbunyang.domain.property.dto.request.AreaRequest;
import subscribers.clearbunyang.domain.property.dto.request.KeywordRequest;
import subscribers.clearbunyang.domain.property.dto.request.PropertySaveRequest;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordName;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.global.file.dto.FileRequestDTO;
import subscribers.clearbunyang.global.file.entity.enums.FileType;

public class PropertySaveRequestDTOFixture {

    public static PropertySaveRequest createDefault() {
        List<AreaRequest> areas = new ArrayList<>();
        //        areas.add(new AreaRequestDTO(60, 50000, 45000, 10));
        //        areas.add(new AreaRequestDTO(80, 60000, 55000, 8));
        areas.add(new AreaRequest(60, 50000, null, 13));
        areas.add(new AreaRequest(80, 60000, null, 11));

        List<FileRequestDTO> files = new ArrayList<>();
        files.add(
                new FileRequestDTO(
                        "property_image.jpg",
                        "https://example.com/image.jpg",
                        FileType.PROPERTY_IMAGE));
        files.add(
                new FileRequestDTO(
                        "supply_information.pdf",
                        "https://example.com/supply.pdf",
                        FileType.SUPPLY_INFORMATION));

        List<KeywordRequest> keywords = new ArrayList<>();
        keywords.add(new KeywordRequest(KeywordName.CASH_PAYMENT, KeywordType.BENEFIT, true, 100));
        keywords.add(
                new KeywordRequest(KeywordName.DISCOUNT_SALE, KeywordType.BENEFIT, true, null));
        keywords.add(
                new KeywordRequest(KeywordName.GUARANTEED_PAYMENT, KeywordType.BENEFIT, false, 13));
        keywords.add(
                new KeywordRequest(
                        KeywordName.SUBWAY,
                        KeywordType.INFRA,
                        true,
                        Map.of("input1", "강남역", "input2", "도보", "input3", "10")));
        List<Map<String, String>> keywordDetails =
                List.of(
                        Map.of("input1", "가족 도서관", "input2", "도보", "input3", "10"),
                        Map.of("input1", "강남 도서관", "input2", "차량", "input3", "15"));
        keywords.add(
                new KeywordRequest(KeywordName.LIBRARY, KeywordType.INFRA, false, keywordDetails));

        return new PropertySaveRequest(
                "푸르지오 아파트",
                "현대건설",
                "현대자이",
                100,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                PropertyType.APARTMENT,
                SalesType.PUBLIC_SALE,
                "서울시 강남구 역삼동 123",
                "서울시",
                "강남구",
                "역삼동",
                "푸르지오 아파트",
                "서울시 강남구 역삼동 456",
                "01012345678",
                null,
                "https://kakao.com/channel",
                areas,
                files,
                keywords);
    }

    public static PropertySaveRequest createCustom(String name, String imageUrl) {
        List<AreaRequest> areas = new ArrayList<>();
        //        areas.add(new AreaRequestDTO(60, 50000, 45000, 10));
        //        areas.add(new AreaRequestDTO(80, 60000, 55000, 8));
        areas.add(new AreaRequest(60, 50000, null, null));
        areas.add(new AreaRequest(80, 60000, null, null));

        List<FileRequestDTO> files = new ArrayList<>();
        files.add(new FileRequestDTO("property_image.jpg", imageUrl, FileType.PROPERTY_IMAGE));
        files.add(
                new FileRequestDTO(
                        "supply_information.pdf",
                        "https://example.com/supply.pdf",
                        FileType.SUPPLY_INFORMATION));

        List<KeywordRequest> keywords = new ArrayList<>();
        keywords.add(new KeywordRequest(KeywordName.CASH_PAYMENT, KeywordType.BENEFIT, true, 100));
        keywords.add(
                new KeywordRequest(KeywordName.DISCOUNT_SALE, KeywordType.BENEFIT, true, null));
        keywords.add(
                new KeywordRequest(KeywordName.GUARANTEED_PAYMENT, KeywordType.BENEFIT, false, 13));
        keywords.add(
                new KeywordRequest(
                        KeywordName.SUBWAY,
                        KeywordType.INFRA,
                        true,
                        Map.of("input1", "강남역", "input2", "도보", "input3", "10")));
        List<Map<String, String>> keywordDetails =
                List.of(
                        Map.of("input1", "가족 도서관", "input2", "도보", "input3", "10"),
                        Map.of("input1", "강남 도서관", "input2", "차량", "input3", "15"));
        keywords.add(
                new KeywordRequest(KeywordName.LIBRARY, KeywordType.INFRA, false, keywordDetails));

        return new PropertySaveRequest(
                name,
                "현대건설",
                "현대자이",
                100,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                PropertyType.APARTMENT,
                SalesType.PUBLIC_SALE,
                "서울시 강남구 역삼동 123",
                "서울시",
                "강남구",
                "역삼동",
                "푸르지오 아파트",
                "서울시 강남구 역삼동 456",
                "01012345678",
                null,
                "https://kakao.com/channel",
                areas,
                files,
                keywords);
    }

    public static PropertySaveRequest createCustom(
            String name, String imageUrl, List<AreaRequest> newAreas) {
        List<AreaRequest> areas = new ArrayList<>();
        areas.addAll(newAreas);

        List<FileRequestDTO> files = new ArrayList<>();
        files.add(new FileRequestDTO("property_image.jpg", imageUrl, FileType.PROPERTY_IMAGE));
        files.add(
                new FileRequestDTO(
                        "supply_information.pdf",
                        "https://example.com/supply.pdf",
                        FileType.SUPPLY_INFORMATION));

        List<KeywordRequest> keywords = new ArrayList<>();
        keywords.add(new KeywordRequest(KeywordName.CASH_PAYMENT, KeywordType.BENEFIT, true, 100));
        keywords.add(
                new KeywordRequest(KeywordName.DISCOUNT_SALE, KeywordType.BENEFIT, true, null));
        keywords.add(
                new KeywordRequest(KeywordName.GUARANTEED_PAYMENT, KeywordType.BENEFIT, false, 13));
        keywords.add(
                new KeywordRequest(
                        KeywordName.SUBWAY,
                        KeywordType.INFRA,
                        true,
                        Map.of("input1", "강남역", "input2", "도보", "input3", "10")));
        List<Map<String, String>> keywordDetails =
                List.of(
                        Map.of("input1", "가족 도서관", "input2", "도보", "input3", "10"),
                        Map.of("input1", "강남 도서관", "input2", "차량", "input3", "15"));
        keywords.add(
                new KeywordRequest(KeywordName.LIBRARY, KeywordType.INFRA, false, keywordDetails));

        return new PropertySaveRequest(
                name,
                "현대건설",
                "현대자이",
                100,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                PropertyType.APARTMENT,
                SalesType.PUBLIC_SALE,
                "서울시 강남구 역삼동 123",
                "서울시",
                "강남구",
                "역삼동",
                "푸르지오 아파트",
                "서울시 강남구 역삼동 456",
                "01012345678",
                null,
                "https://kakao.com/channel",
                areas,
                files,
                keywords);
    }
}
