package subscribers.clearbunyang.s3;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/preSigned-url")
    public String getPreSignedUrl(
            @RequestParam String directoryName, @RequestParam String fileName) {
        return fileService.getPreSignedUrl(directoryName, fileName);
    }
}
