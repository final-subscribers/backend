package subscribers.clearbunyang.domain.file.service;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {
    @Value("${aws.s3.bucket}") private String bucket;

    @Value("${aws.s3.accountId}") private String accountId;

    private final AmazonS3 amazonS3;

    /**
     * presigned url 발급
     *
     * @param directoryName 저장할 디렉토리 이름
     * @param fileName 클라이언트가 전달한 파일명
     * @return presigned url
     */
    public String getPreSignedUrl(String directoryName, String fileName) {
        fileName = createPath(directoryName, fileName);
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                getGeneratePreSignedUrlRequest(bucket, fileName);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    /**
     * 파일 업로드용(PUT) presigned url 생성
     *
     * @param bucket 버킷 이름
     * @param fileName S3 업로드용 파일 이름
     * @return presigned url
     */
    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(
            String bucket, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    /**
     * presigned url 유효 기간 설정
     *
     * @return 유효기간
     */
    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2; // 2분
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    /**
     * 파일 고유 ID를 생성
     *
     * @return 36자리의 UUID
     */
    private String createFileId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 파일의 전체 경로를 생성
     *
     * @param 디렉토리 경로
     * @return 파일의 전체 경로
     */
    private String createPath(String directoryName, String fileName) {
        String fileId = createFileId();
        return String.format("%s/%s:%s", directoryName, fileId, fileName);
    }

    /**
     * 저장된 파일에 접근할 수 있는 URL 리턴
     *
     * @param fileName 파일 이름
     * @param directoryName 디렉토리 이름
     * @return
     */
    public String getFile(String fileName, String directoryName) {
        return String.format(
                "https://%s.r2.cloudflarestorage.com/%s/%s/%s",
                accountId, bucket, directoryName, fileName);
    }
}
