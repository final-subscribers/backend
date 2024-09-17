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
import subscribers.clearbunyang.domain.property.model.request.PropertyUpdateRequestDTO;

public class PropertyUpdateRequestDTOFixture {

    public static PropertyUpdateRequestDTO createDefault() {
        List<AreaRequestDTO> areas = new ArrayList<>();
        areas.add(new AreaRequestDTO(60, 50000, null, null));
        areas.add(new AreaRequestDTO(80, 60000, null, null));

        FileRequestDTO propertyImage =
                new FileRequestDTO(
                        "property_image.jpg", "https://propertyImage.jpg", FileType.PROPERTY_IMAGE);
        FileRequestDTO supplyInfornation =
                new FileRequestDTO(
                        "supply_information.pdf",
                        "https://example.com/supply.pdf",
                        FileType.SUPPLY_INFORMATION);

        List<KeywordRequestDTO> infraKeywords = new ArrayList<>();
        List<KeywordRequestDTO> benefitKeywords = new ArrayList<>();
        benefitKeywords.add(
                new KeywordRequestDTO(KeywordName.CASH_PAYMENT, KeywordType.BENEFIT, true, 100));
        benefitKeywords.add(
                new KeywordRequestDTO(KeywordName.DISCOUNT_SALE, KeywordType.BENEFIT, true, null));
        benefitKeywords.add(
                new KeywordRequestDTO(
                        KeywordName.GUARANTEED_PAYMENT, KeywordType.BENEFIT, false, 13));
        infraKeywords.add(
                new KeywordRequestDTO(
                        KeywordName.SUBWAY,
                        KeywordType.INFRA,
                        true,
                        Map.of("input1", "강남역", "input2", "도보", "input3", "10")));
        List<Map<String, String>> keywordDetails =
                List.of(
                        Map.of("input1", "가족 도서관", "input2", "도보", "input3", "10"),
                        Map.of("input1", "강남 도서관", "input2", "차량", "input3", "15"));
        infraKeywords.add(
                new KeywordRequestDTO(
                        KeywordName.LIBRARY, KeywordType.INFRA, false, keywordDetails));

        return new PropertyUpdateRequestDTO(
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

    public static PropertyUpdateRequestDTO createDefault2() {
        List<AreaRequestDTO> areas = new ArrayList<>();
        areas.add(new AreaRequestDTO(6022, 50022, 22, null));
        areas.add(new AreaRequestDTO(8022, 60022, 22, null));

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

        List<KeywordRequestDTO> infraKeywords = new ArrayList<>();
        List<KeywordRequestDTO> benefitKeywords = new ArrayList<>();
        benefitKeywords.add(
                new KeywordRequestDTO(KeywordName.CASH_PAYMENT, KeywordType.BENEFIT, true, 22));
        benefitKeywords.add(
                new KeywordRequestDTO(KeywordName.DISCOUNT_SALE, KeywordType.BENEFIT, true, null));
        benefitKeywords.add(
                new KeywordRequestDTO(
                        KeywordName.GUARANTEED_PAYMENT, KeywordType.BENEFIT, false, 22));
        infraKeywords.add(
                new KeywordRequestDTO(
                        KeywordName.SUBWAY,
                        KeywordType.INFRA,
                        true,
                        Map.of("input1", "강남역22", "input2", "도보", "input3", "10")));
        List<Map<String, String>> keywordDetails =
                List.of(
                        Map.of("input1", "가족 도서관22", "input2", "도보", "input3", "10"),
                        Map.of("input1", "강남 도서관22", "input2", "차량", "input3", "15"));
        infraKeywords.add(
                new KeywordRequestDTO(
                        KeywordName.LIBRARY, KeywordType.INFRA, false, keywordDetails));

        return new PropertyUpdateRequestDTO(
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
