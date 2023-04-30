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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.cloudfront.internal.auth.Pem;
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.ssm.SsmClient;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageServiceImpl implements ImageService {

  final CloudFrontUtilities cloudFrontUtilities;
  final SsmClient ssmClient;
  final S3Client s3Client;

  final Instant expirationDate = Clock.systemUTC().instant().plus(Duration.ofHours(3));

  @Value("${private.key.name}")
  private String privateKeyName;

  @Value("${cloudfront.domain.private}")
  private String cloudfrontDomainPrivate;

  @Value("${cloudfront.domain.public}")
  private String cloudfrontDomainPublic;

  @Value("${cloudfront.key_pair_id}")
  private String keyPairId;

  @Value("${s3.bucket.name}")
  private String s3Bucket;

  @Override
  public String getPublicImageUrl(final String imageUrl) {
    return "https://" + cloudfrontDomainPublic + "/" + imageUrl;
  }

  @Override
  public String getSignedImageUrl(final String imageUrl) {
    final CannedSignerRequest cannedRequest = CannedSignerRequest.builder()
        .resourceUrl("https://" + cloudfrontDomainPrivate + "/" + imageUrl)
        .privateKey(generatePrivateKey())
        .keyPairId(keyPairId)
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
        .bucket(s3Bucket)
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
    return ssmClient.getParameter(
        r -> r.name(privateKeyName).withDecryption(true)).parameter().value();
  }

}
