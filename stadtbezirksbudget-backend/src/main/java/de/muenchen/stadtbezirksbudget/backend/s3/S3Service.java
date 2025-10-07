package de.muenchen.stadtbezirksbudget.backend.s3;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Service
public class S3Service {

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucket;

    @Autowired
    private S3Client s3Client;

    public byte[] download(String filename) throws IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(filename)
                .build();

        try (ResponseInputStream<GetObjectResponse> stream = s3Client.getObject(request)) {
            return stream.readAllBytes();
        }
    }
}

