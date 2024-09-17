package subscribers.clearbunyang.testfixtures;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;
import subscribers.clearbunyang.domain.file.model.FileRequestDTO;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordName;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.property.model.request.AreaRequestDTO;
import subscribers.clearbunyang.domain.property.model.request.KeywordRequestDTO;
import subscribers.clearbunyang.domain.property.model.request.PropertySaveRequestDTO;

public class PropertySaveRequestDTOFixture {

    public static PropertySaveRequestDTO createDefault() {
        List<AreaRequestDTO> areas = new ArrayList<>();
        //        areas.add(new AreaRequestDTO(60, 50000, 45000, 10));
        //        areas.add(new AreaRequestDTO(80, 60000, 55000, 8));
        areas.add(new AreaRequestDTO(60, 50000, null, null));
        areas.add(new AreaRequestDTO(80, 60000, null, null));

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

        List<KeywordRequestDTO> keywords = new ArrayList<>();
        keywords.add(
                new KeywordRequestDTO(KeywordName.CASH_PAYMENT, KeywordType.BENEFIT, true, 100));
        keywords.add(
                new KeywordRequestDTO(KeywordName.DISCOUNT_SALE, KeywordType.BENEFIT, true, null));
        keywords.add(
                new KeywordRequestDTO(
                        KeywordName.GUARANTEED_PAYMENT, KeywordType.BENEFIT, false, 13));
        keywords.add(
                new KeywordRequestDTO(
                        KeywordName.SUBWAY,
                        KeywordType.INFRA,
                        true,
                        Map.of("input1", "강남역", "input2", "도보", "input3", "10")));
        List<Map<String, String>> keywordDetails =
                List.of(
                        Map.of("input1", "가족 도서관", "input2", "도보", "input3", "10"),
                        Map.of("input1", "강남 도서관", "input2", "차량", "input3", "15"));
        keywords.add(
                new KeywordRequestDTO(
                        KeywordName.LIBRARY, KeywordType.INFRA, false, keywordDetails));

        return new PropertySaveRequestDTO(
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

    public static PropertySaveRequestDTO createCustom(String name, String imageUrl) {
        List<AreaRequestDTO> areas = new ArrayList<>();
        //        areas.add(new AreaRequestDTO(60, 50000, 45000, 10));
        //        areas.add(new AreaRequestDTO(80, 60000, 55000, 8));
        areas.add(new AreaRequestDTO(60, 50000, null, null));
        areas.add(new AreaRequestDTO(80, 60000, null, null));

        List<FileRequestDTO> files = new ArrayList<>();
        files.add(new FileRequestDTO("property_image.jpg", imageUrl, FileType.PROPERTY_IMAGE));
        files.add(
                new FileRequestDTO(
                        "supply_information.pdf",
                        "https://example.com/supply.pdf",
                        FileType.SUPPLY_INFORMATION));

        List<KeywordRequestDTO> keywords = new ArrayList<>();
        keywords.add(
                new KeywordRequestDTO(KeywordName.CASH_PAYMENT, KeywordType.BENEFIT, true, 100));
        keywords.add(
                new KeywordRequestDTO(KeywordName.DISCOUNT_SALE, KeywordType.BENEFIT, true, null));
        keywords.add(
                new KeywordRequestDTO(
                        KeywordName.GUARANTEED_PAYMENT, KeywordType.BENEFIT, false, 13));
        keywords.add(
                new KeywordRequestDTO(
                        KeywordName.SUBWAY,
                        KeywordType.INFRA,
                        true,
                        Map.of("input1", "강남역", "input2", "도보", "input3", "10")));
        List<Map<String, String>> keywordDetails =
                List.of(
                        Map.of("input1", "가족 도서관", "input2", "도보", "input3", "10"),
                        Map.of("input1", "강남 도서관", "input2", "차량", "input3", "15"));
        keywords.add(
                new KeywordRequestDTO(
                        KeywordName.LIBRARY, KeywordType.INFRA, false, keywordDetails));

        return new PropertySaveRequestDTO(
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

    public static PropertySaveRequestDTO createCustom(
            String name, String imageUrl, List<AreaRequestDTO> newAreas) {
        List<AreaRequestDTO> areas = new ArrayList<>();
        areas.addAll(newAreas);

        List<FileRequestDTO> files = new ArrayList<>();
        files.add(new FileRequestDTO("property_image.jpg", imageUrl, FileType.PROPERTY_IMAGE));
        files.add(
                new FileRequestDTO(
                        "supply_information.pdf",
                        "https://example.com/supply.pdf",
                        FileType.SUPPLY_INFORMATION));

        List<KeywordRequestDTO> keywords = new ArrayList<>();
        keywords.add(
                new KeywordRequestDTO(KeywordName.CASH_PAYMENT, KeywordType.BENEFIT, true, 100));
        keywords.add(
                new KeywordRequestDTO(KeywordName.DISCOUNT_SALE, KeywordType.BENEFIT, true, null));
        keywords.add(
                new KeywordRequestDTO(
                        KeywordName.GUARANTEED_PAYMENT, KeywordType.BENEFIT, false, 13));
        keywords.add(
                new KeywordRequestDTO(
                        KeywordName.SUBWAY,
                        KeywordType.INFRA,
                        true,
                        Map.of("input1", "강남역", "input2", "도보", "input3", "10")));
        List<Map<String, String>> keywordDetails =
                List.of(
                        Map.of("input1", "가족 도서관", "input2", "도보", "input3", "10"),
                        Map.of("input1", "강남 도서관", "input2", "차량", "input3", "15"));
        keywords.add(
                new KeywordRequestDTO(
                        KeywordName.LIBRARY, KeywordType.INFRA, false, keywordDetails));

        return new PropertySaveRequestDTO(
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
