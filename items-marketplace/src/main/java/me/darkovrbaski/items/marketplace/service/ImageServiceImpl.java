package me.darkovrbaski.items.marketplace.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.PrivateKey;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.model.S3ImageDir;
import me.darkovrbaski.items.marketplace.service.intefaces.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.cloudfront.internal.auth.Pem;
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ImageServiceImpl implements ImageService {

  CloudFrontUtilities cloudFrontUtilities;
  SecretsManagerClient secretsManagerClient;
  S3Client s3Client;

  Instant expirationDate = Instant.now(Clock.systemUTC()).plus(Duration.ofHours(3));
  static String KEY_SECRET_NAME = "items-marketplace-images-private-key";
  static String CLOUDFRONT_DOMAIN = "https://ddeo915oi6sfe.cloudfront.net/";
  static String PUBLIC_CLOUDFRONT_DOMAIN = "https://dy4fqujjkm2dt.cloudfront.net/";
  static String KEY_PAIR_ID = "KHKCBDW0AB4E9";
  static String S3_BUCKET = "items-marketplace-images";

  @Override
  public String getPublicImageUrl(final String imageUrl) {
    return PUBLIC_CLOUDFRONT_DOMAIN + imageUrl;
  }

  @Override
  public String getSignedImageUrl(final String imageUrl) {
    final CannedSignerRequest cannedRequest = CannedSignerRequest.builder()
        .resourceUrl(CLOUDFRONT_DOMAIN + imageUrl)
        .privateKey(generatePrivateKey())
        .keyPairId(KEY_PAIR_ID)
        .expirationDate(expirationDate)
        .build();

    return cloudFrontUtilities.getSignedUrlWithCannedPolicy(cannedRequest).url();
  }

  @SneakyThrows
  @Override
  public void saveImage(final MultipartFile file, final String imageName,
      final S3ImageDir imageDir) {
    if (file.getSize() > 5_000_000) {
      throw new IllegalArgumentException("File size is bigger than 5MB");
    }
    
    final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(S3_BUCKET)
        .key(imageDir + imageName)
        .contentType(file.getContentType())
        .contentLength(file.getSize())
        .build();

    s3Client.putObject(putObjectRequest,
        RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
  }

  @SneakyThrows
  private PrivateKey generatePrivateKey() {
    final InputStream secretKeyBytes = new ByteArrayInputStream(requestSecretKey().getBytes());
    return Pem.readPrivateKey(secretKeyBytes);
  }

  private String requestSecretKey() {
    return secretsManagerClient.getSecretValue(r -> r.secretId(KEY_SECRET_NAME)).secretString();
  }

}
