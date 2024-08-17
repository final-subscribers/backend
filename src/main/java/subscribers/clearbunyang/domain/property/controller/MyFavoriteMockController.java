package subscribers.clearbunyang.domain.property.controller;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/member/my-favorites")
@RequiredArgsConstructor
public class MyFavoriteMockController {

    @GetMapping
    public Map<String, Object> getMyFavorites() {

        List<List<String>> keywords =
                Arrays.asList(
                        Arrays.asList("할인분양", "혜택"),
                        Arrays.asList("지하철", "편의시설"),
                        Arrays.asList("버스", "편의시설"));

        // 매물 정보
        List<Map<String, Object>> properties =
                Arrays.asList(
                        new HashMap<String, Object>() {
                            {
                                put("keyword", keywords);
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("name", "잠실 프루지오");
                                put("area_addr", "서울특별시 서초구 ");
                                put("created_at", "2024-07-20");
                                put("property_type", "아파트");
                                put("sales_type", "민간 분양");
                                put("count", 100);
                                put("price", 29000);
                                put("sales_price", 27000);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("keyword", keywords);
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("name", "잠실 프루지오");
                                put("area_addr", "서울특별시 서초구 ");
                                put("created_at", "2024-07-20");
                                put("property_type", "아파트");
                                put("sales_type", "민간 분양");
                                put("count", 100);
                                put("price", 29000);
                                put("sales_price", 27000);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("keyword", keywords);
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("name", "잠실 프루지오");
                                put("area_addr", "서울특별시 서초구 ");
                                put("created_at", "2024-07-20");
                                put("property_type", "아파트");
                                put("sales_type", "민간 분양");
                                put("count", 100);
                                put("price", 29000);
                                put("sales_price", 27000);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("keyword", keywords);
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("name", "잠실 프루지오");
                                put("area_addr", "서울특별시 서초구 ");
                                put("created_at", "2024-07-20");
                                put("property_type", "아파트");
                                put("sales_type", "민간 분양");
                                put("count", 100);
                                put("price", 29000);
                                put("sales_price", 27000);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("keyword", keywords);
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("name", "잠실 프루지오");
                                put("area_addr", "서울특별시 서초구 ");
                                put("created_at", "2024-07-20");
                                put("property_type", "아파트");
                                put("sales_type", "민간 분양");
                                put("count", 100);
                                put("price", 29000);
                                put("sales_price", 27000);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("keyword", keywords);
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("name", "잠실 프루지오");
                                put("area_addr", "서울특별시 서초구 ");
                                put("created_at", "2024-07-20");
                                put("property_type", "아파트");
                                put("sales_type", "민간 분양");
                                put("count", 100);
                                put("price", 29000);
                                put("sales_price", 27000);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("keyword", keywords);
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("name", "잠실 프루지오");
                                put("area_addr", "서울특별시 서초구 ");
                                put("created_at", "2024-07-20");
                                put("property_type", "아파트");
                                put("sales_type", "민간 분양");
                                put("count", 100);
                                put("price", 29000);
                                put("sales_price", 27000);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("keyword", keywords);
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("name", "잠실 프루지오");
                                put("area_addr", "서울특별시 서초구 ");
                                put("created_at", "2024-07-20");
                                put("property_type", "아파트");
                                put("sales_type", "민간 분양");
                                put("count", 100);
                                put("price", 29000);
                                put("sales_price", 27000);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("keyword", keywords);
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("name", "잠실 프루지오");
                                put("area_addr", "서울특별시 서초구 ");
                                put("created_at", "2024-07-20");
                                put("property_type", "아파트");
                                put("sales_type", "민간 분양");
                                put("count", 100);
                                put("price", 29000);
                                put("sales_price", 27000);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("keyword", keywords);
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("name", "잠실 프루지오");
                                put("area_addr", "서울특별시 서초구 ");
                                put("created_at", "2024-07-20");
                                put("property_type", "아파트");
                                put("sales_type", "민간 분양");
                                put("count", 100);
                                put("price", 29000);
                                put("sales_price", 27000);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("keyword", keywords);
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("name", "잠실 프루지오");
                                put("area_addr", "서울특별시 서초구 ");
                                put("created_at", "2024-07-20");
                                put("property_type", "아파트");
                                put("sales_type", "민간 분양");
                                put("count", 100);
                                put("price", 29000);
                                put("sales_price", 27000);
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put("keyword", keywords);
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("name", "잠실 프루지오");
                                put("area_addr", "서울특별시 서초구 ");
                                put("created_at", "2024-07-20");
                                put("property_type", "아파트");
                                put("sales_type", "민간 분양");
                                put("count", 100);
                                put("price", 29000);
                                put("sales_price", 27000);
                            }
                        });

        Map<String, Object> response = new HashMap<>();
        response.put("favorite_number", 21);
        response.put("properties", properties);

        return response;
    }
}
