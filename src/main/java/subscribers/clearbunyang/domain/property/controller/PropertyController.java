package subscribers.clearbunyang.domain.property.controller;


import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.model.KeywordDTO;
import subscribers.clearbunyang.domain.property.model.PropertyRequestDTO;
import subscribers.clearbunyang.domain.property.service.PropertyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/properties")
public class PropertyController {

    private final PropertyService propertyService;

    //    @PostMapping("/add-test")
    //    public ResponseEntity add(@RequestBody KeywordsRequestDTO requestBody) {
    //        try {
    //            // 서비스 계층에 전달하여 JSON 데이터를 저장
    //            propertyService.saveKeywords(requestBody.getKeywords());
    //            return ResponseEntity.ok("Success");
    //        } catch (Exception e) {
    //            return ResponseEntity.status(500).body("Error: " + e.getMessage());
    //        }
    //    }
    @PostMapping("")
    public ResponseEntity addProperty(@Valid @RequestBody PropertyRequestDTO requestDTO) {
        // 서비스 계층에 전달하여 JSON 데이터를 저장
        Property property = propertyService.saveProperty(requestDTO);
        System.out.println(property);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("")
    public ResponseEntity get(@RequestParam Long propertyId) {
        Map<String, List<KeywordDTO>> stringListMap =
                propertyService.categorizedKeywords(propertyId);
        return ResponseEntity.ok(stringListMap);
    }
}
