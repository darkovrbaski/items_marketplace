package me.darkovrbaski.items.marketplace.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum S3ImageDir {

  IMAGES("images/"),
  PUBLIC_IMAGES("public_images/");

  String directory;

  @Override
  public String toString() {
    return directory;
  }

}
