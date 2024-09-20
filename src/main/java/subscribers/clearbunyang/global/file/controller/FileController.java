package subscribers.clearbunyang.global.file.controller;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import subscribers.clearbunyang.global.file.dto.PresignedUrlRequestDTO;
import subscribers.clearbunyang.global.file.service.FileService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common")
public class FileController {
    private final FileService fileService;

    /**
     * 여러 파일들에 대한 presigned-url 리스트를 리턴
     *
     * @param requestDTO
     * @return presigned-url 문자열 리스트
     */
    @PostMapping("presigned-url")
    public List<String> getPreSignedUrl(@RequestBody PresignedUrlRequestDTO requestDTO) {
        return requestDTO.getFiles().stream()
                .map(
                        uploadFile ->
                                fileService.getPreSignedUrl(
                                        uploadFile.getType().name(), uploadFile.getName()))
                .collect(Collectors.toList());
    }
}
