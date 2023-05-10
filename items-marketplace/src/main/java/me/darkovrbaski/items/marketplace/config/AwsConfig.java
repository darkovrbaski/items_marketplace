package me.darkovrbaski.items.marketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ssm.SsmClient;

@Configuration
public class AwsConfig {

  @Bean
  public CloudFrontUtilities cloudFrontClient() {
    return CloudFrontUtilities.create();
  }

  @Bean
  public S3Client s3Client() {
    return S3Client.create();
  }

  @Bean
  public SsmClient secretsManagerClient() {
    return SsmClient.create();
  }

}
