package me.darkovrbaski.items.marketplace.service.intefaces;

import me.darkovrbaski.items.marketplace.model.S3ImageDir;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

  String getPublicImageUrl(String imageUrl);

  String getSignedImageUrl(String imageUrl);

  void saveImage(MultipartFile file, String imageName, S3ImageDir imageDir);

}
