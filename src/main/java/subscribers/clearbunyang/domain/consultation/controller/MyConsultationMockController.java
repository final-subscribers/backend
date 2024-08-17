package subscribers.clearbunyang.domain.consultation.controller;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/member/my-consultations")
@RequiredArgsConstructor
public class MyConsultationMockController {

    // 상담 대기, 완료 모두 동일함
    @GetMapping
    public Map<String, Object> getMyConsultations() {

        // 상담 내역
        List<Map<String, Object>> consultations =
                Arrays.asList(
                        new HashMap<String, Object>() {
                            {
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("id", 1);
                                put("name", "잠실 프루지오");
                                put("consultation_created_at", "2024-07-20");
                                put("message", "상담 신청합니다!");
                                put("member_name", "박지선");
                                put("phone_number", "010-1234-5678");
                                put("preferred_at", "2024-07-21");
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("id", 2);
                                put("name", "잠실 프루지오");
                                put("consultation_created_at", "2024-07-20");
                                put("message", "상담 신청합니다!");
                                put("member_name", "박지선");
                                put("phone_number", "010-1234-5678");
                                put("preferred_at", "2024-07-21");
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("id", 3);
                                put("name", "잠실 프루지오");
                                put("consultation_created_at", "2024-07-20");
                                put("message", "상담 신청합니다!");
                                put("member_name", "박지선");
                                put("phone_number", "010-1234-5678");
                                put("preferred_at", "2024-07-21");
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("id", 4);
                                put("name", "잠실 프루지오");
                                put("consultation_created_at", "2024-07-20");
                                put("message", "상담 신청합니다!");
                                put("member_name", "박지선");
                                put("phone_number", "010-1234-5678");
                                put("preferred_at", "2024-07-21");
                            }
                        },
                        new HashMap<String, Object>() {
                            {
                                put(
                                        "image_url",
                                        "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
                                put("id", 5);
                                put("name", "잠실 프루지오");
                                put("consultation_created_at", "2024-07-20");
                                put("message", "상담 신청합니다!");
                                put("member_name", "박지선");
                                put("phone_number", "010-1234-5678");
                                put("preferred_at", "2024-07-21");
                            }
                        });

        Map<String, Object> response = new HashMap<>();
        response.put("count", 21);
        response.put("properties", consultations);

        return response;
    }
}
