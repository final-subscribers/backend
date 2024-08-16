package subscribers.clearbunyang.domain.home.controller;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class HomeController {

    @GetMapping("/api/mock/common/home")
    public Map<String, Object> getHomePage(@RequestParam int page) {

        List<String> homeImagesUrl =
                Arrays.asList(
                        "https://down.humoruniv.com//hwiparambbs/data/editor/pdswait/e_s941967001_54424d83ad875aebff802c182e0bb4d96e88a6fb.jpg",
                        "https://down.humoruniv.com//hwiparambbs/data/editor/pdswait/e_s941967002_b83b74ed967105c20c99d8686749ba9efc5ef607.jpg",
                        "https://down.humoruniv.com//hwiparambbs/data/editor/pdswait/e_s941967004_441b9e09e1edf1d1b1dd412ab24c429936bfa41c.jpg");

        List<Map<String, Object>> properties =
                Arrays.asList(
                        new HashMap<String, Object>() {
                            {
                                put("id", 13);
                                put(
                                        "image_url",
                                        "https://down.humoruniv.com//hwiparambbs/data/editor/pdswait/e_s941967005_304f24d0edf2388f1b620b937c97519783db7975.jpg");
                                put("property_name", "계양 어떤서원");
                                put("area_addr", "인천시 계양구 용종로");
                                put("property_type", "아파트");
                                put("sales_type", "민간분양");
                                put("total_number", 100);
                                put("keywords", Arrays.asList("할인 분양", "대중교통", "의료시설"));
                                put("price", 29800);
                                put("discount_price", 29800);
                                put("like", true);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("id", 14);
                                put(
                                        "image_url",
                                        "https://down.humoruniv.com//hwiparambbs/data/editor/pdswait/e_s941967008_f251d71f486ed2665980bf3016d7fee8315835ad.jpg");
                                put("property_name", "송도 해오름마을");
                                put("area_addr", "인천시 연수구 해돋이로");
                                put("property_type", "아파트");
                                put("sales_type", "공공분양");
                                put("total_number", 200);
                                put("keywords", Arrays.asList("바다 전망", "교육시설", "대형 쇼핑몰"));
                                put("price", 38500);
                                put("discount_price", 37000);
                                put("like", false);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("id", 15);
                                put(
                                        "image_url",
                                        "https://down.humoruniv.com//hwiparambbs/data/editor/pdswait/e_s941967012_776e2b76b27724a275e8076b423a6f4f1055bf55.jpg");
                                put("property_name", "부평 e편한세상");
                                put("area_addr", "인천시 부평구 부평대로");
                                put("property_type", "아파트");
                                put("sales_type", "민간분양");
                                put("total_number", 150);
                                put("keywords", Arrays.asList("할인 분양", "편리한 교통", "공원 인근"));
                                put("price", 32000);
                                put("discount_price", 31000);
                                put("like", true);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("id", 16);
                                put(
                                        "image_url",
                                        "https://down.humoruniv.com//hwiparambbs/data/editor/pdswait/e_s941967013_b1aa33a4d9cec29a9bf41e33048f4b742c219a19.jpg");
                                put("property_name", "청라 더샵 레이크파크");
                                put("area_addr", "인천시 서구 청라대로");
                                put("property_type", "아파트");
                                put("sales_type", "민간분양");
                                put("total_number", 250);
                                put("keywords", Arrays.asList("호수공원", "대형마트", "의료시설"));
                                put("price", 45000);
                                put("discount_price", 43000);
                                put("like", true);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("id", 17);
                                put(
                                        "image_url",
                                        "https://down.humoruniv.com//hwiparambbs/data/editor/pdswait/e_s941967017_3e5e24e4e6d2b9bf35032dcdc36d3ad26a372d25.jpg");
                                put("property_name", "인천시티빌");
                                put("area_addr", "인천시 미추홀구 경원대로");
                                put("property_type", "오피스텔");
                                put("sales_type", "공공임대");
                                put("total_number", 120);
                                put("keywords", Arrays.asList("저렴한 임대", "편리한 교통", "상업지구 인근"));
                                put("price", 22000);
                                put("discount_price", 21000);
                                put("like", false);
                            }
                        });

        Map<String, Object> response = new HashMap<>();
        response.put("HomeImagesUrl", homeImagesUrl);
        response.put("properties", properties);

        return response;
    }

    @GetMapping("/api/mock/common/properties")
    public Map<String, Object> getProperties(
            @RequestParam String area,
            @RequestParam String propertyType,
            @RequestParam String salesType,
            @RequestParam String keyword,
            @RequestParam int price,
            @RequestParam int square_meter,
            @RequestParam int total,
            @RequestParam int size) {

        List<Map<String, Object>> propertyResponseList =
                Arrays.asList(
                        new HashMap<String, Object>() {
                            {
                                put("property_id", 1);
                                put(
                                        "image_url",
                                        "https://down.humoruniv.com//hwiparambbs/data/editor/pdswait/e_s941967018_c5499b4d46d96b111bf3441fcc0067cbb452162e.jpg");
                                put("property_type", "apartment");
                                put("sales_type", "private_sale");
                                put("total_number", 507);
                                put("area", 60);
                                put("price", 29800);
                                put("discount_price", 29800);
                                put("area_addr", "부산시 기장구 장안읍");
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("property_id", 2);
                                put(
                                        "image_url",
                                        "https://down.humoruniv.com//hwiparambbs/data/editor/pdswait/e_s941967020_8f8e3713f96ac01eea0a9aadab49d2b46355cef3.jpg");
                                put("property_type", "apartment");
                                put("sales_type", "private_sale");
                                put("total_number", 507);
                                put("area", 79);
                                put("price", 29800);
                                put("discount_price", 29800);
                                put("area_addr", "부산시 기장구 장안읍");
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("property_id", 3);
                                put(
                                        "image_url",
                                        "https://down.humoruniv.com//hwiparambbs/data/editor/pdswait/e_s941967023_cb7b7b08cd869fa8237a0ad488d0f43fff1caa0d.jpg");
                                put("property_type", "apartment");
                                put("sales_type", "private_sale");
                                put("total_number", 507);
                                put("area", 99);
                                put("price", 29800);
                                put("discount_price", 29800);
                                put("area_addr", "부산시 기장구 장안읍");
                            }
                        });

        Map<String, Object> response = new HashMap<>();
        response.put("property_response_list", propertyResponseList);

        return response;
    }
}
