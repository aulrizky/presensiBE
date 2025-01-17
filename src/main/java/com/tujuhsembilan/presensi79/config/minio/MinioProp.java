package com.tujuhsembilan.presensi79.config.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("application.minio")
public class MinioProp {
    private String url;
    private String username;
    private String password;
    private String bucketName;
    private String getErrorMessage;
    private String postErrorMessage;
}
