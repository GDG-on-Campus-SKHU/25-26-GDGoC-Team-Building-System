package com.skhu.gdgocteambuildingproject.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    private static final Region REGION = Region.AP_NORTHEAST_2;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(REGION)
                .build();
    }
}
