package subscribers.clearbunyang.domain.file.controller;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import subscribers.clearbunyang.domain.file.model.PresignedUrlRequestDTO;
import subscribers.clearbunyang.domain.file.service.FileService;

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
    // todo 배포된 프론트에서 해당 URL에 접근할 수 있는지 확인하기
    @GetMapping("/loadFile")
    public String loadFile(@RequestParam String fileName, @RequestParam String directoryName) {
        return fileService.getFile(fileName, directoryName);
    }
}
