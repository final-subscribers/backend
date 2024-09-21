package subscribers.clearbunyang.testfixtures;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import subscribers.clearbunyang.domain.property.dto.request.AreaRequest;
import subscribers.clearbunyang.domain.property.dto.request.KeywordRequest;
import subscribers.clearbunyang.domain.property.dto.request.PropertyUpdateRequest;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordName;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.global.file.dto.FileRequestDTO;
import subscribers.clearbunyang.global.file.entity.enums.FileType;

public class PropertyUpdateRequestDTOFixture {

    public static PropertyUpdateRequest createDefault() {
        List<AreaRequest> areas = new ArrayList<>();
        areas.add(new AreaRequest(60, 50000, 40000, 13));
        areas.add(new AreaRequest(80, 60000, 50000, 11));

        FileRequestDTO propertyImage =
                new FileRequestDTO(
                        "property_image.jpg", "https://propertyImage.jpg", FileType.PROPERTY_IMAGE);
        FileRequestDTO supplyInfornation =
                new FileRequestDTO(
                        "supply_information.pdf",
                        "https://example.com/supply.pdf",
                        FileType.SUPPLY_INFORMATION);

        List<KeywordRequest> infraKeywords = new ArrayList<>();
        List<KeywordRequest> benefitKeywords = new ArrayList<>();
        benefitKeywords.add(
                new KeywordRequest(KeywordName.CASH_PAYMENT, KeywordType.BENEFIT, true, 100));
        benefitKeywords.add(
                new KeywordRequest(KeywordName.DISCOUNT_SALE, KeywordType.BENEFIT, true, null));
        benefitKeywords.add(
                new KeywordRequest(KeywordName.GUARANTEED_PAYMENT, KeywordType.BENEFIT, false, 13));
        infraKeywords.add(
                new KeywordRequest(
                        KeywordName.SUBWAY,
                        KeywordType.INFRA,
                        true,
                        Map.of("input1", "강남역", "input2", "도보", "input3", "10")));
        List<Map<String, String>> keywordDetails =
                List.of(
                        Map.of("input1", "가족 도서관", "input2", "도보", "input3", "10"),
                        Map.of("input1", "강남 도서관", "input2", "차량", "input3", "15"));
        infraKeywords.add(
                new KeywordRequest(KeywordName.LIBRARY, KeywordType.INFRA, false, keywordDetails));

        return new PropertyUpdateRequest(
                "계양 학마을",
                SalesType.PUBLIC_SALE,
                PropertyType.APARTMENT,
                "시공사",
                "시행사",
                "서울시",
                "강남구",
                "역삼동",
                "푸르지오 아파트",
                "서울시 강남구 역삼동 123",
                320,
                "모델하우스 주소",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                "https://kakao.com/channel",
                null,
                "01012345678",
                areas,
                propertyImage,
                supplyInfornation,
                null,
                infraKeywords,
                benefitKeywords);
    }

    public static PropertyUpdateRequest createDefault2() {
        List<AreaRequest> areas = new ArrayList<>();
        areas.add(new AreaRequest(6022, 50022, 22, null));
        areas.add(new AreaRequest(8022, 60022, 22, null));

        FileRequestDTO propertyImage =
                new FileRequestDTO(
                        "property_image222.jpg",
                        "https://propertyImage2222.jpg",
                        FileType.PROPERTY_IMAGE);
        FileRequestDTO supplyInfornation =
                new FileRequestDTO(
                        "supply_information2222.pdf",
                        "https://example.com/supply2222.pdf",
                        FileType.SUPPLY_INFORMATION);

        List<KeywordRequest> infraKeywords = new ArrayList<>();
        List<KeywordRequest> benefitKeywords = new ArrayList<>();
        benefitKeywords.add(
                new KeywordRequest(KeywordName.CASH_PAYMENT, KeywordType.BENEFIT, true, 22));
        benefitKeywords.add(
                new KeywordRequest(KeywordName.DISCOUNT_SALE, KeywordType.BENEFIT, true, null));
        benefitKeywords.add(
                new KeywordRequest(KeywordName.GUARANTEED_PAYMENT, KeywordType.BENEFIT, false, 22));
        infraKeywords.add(
                new KeywordRequest(
                        KeywordName.SUBWAY,
                        KeywordType.INFRA,
                        true,
                        Map.of("input1", "강남역22", "input2", "도보", "input3", "10")));
        List<Map<String, String>> keywordDetails =
                List.of(
                        Map.of("input1", "가족 도서관22", "input2", "도보", "input3", "10"),
                        Map.of("input1", "강남 도서관22", "input2", "차량", "input3", "15"));
        infraKeywords.add(
                new KeywordRequest(KeywordName.LIBRARY, KeywordType.INFRA, false, keywordDetails));

        return new PropertyUpdateRequest(
                "계양 학마을22",
                SalesType.PUBLIC_SALE,
                PropertyType.APARTMENT,
                "시공사22",
                "시행사22",
                "서울시22",
                "강남구22",
                "역삼동22",
                "푸르지오 아파트22",
                "서울시 강남구 역삼동 12322",
                22,
                "모델하우스 주소22",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                "https://kakao.com/channel22",
                null,
                "01012345622",
                areas,
                propertyImage,
                supplyInfornation,
                null,
                infraKeywords,
                benefitKeywords);
    }
}
