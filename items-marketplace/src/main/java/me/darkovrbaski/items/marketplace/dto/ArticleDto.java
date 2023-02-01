package me.darkovrbaski.items.marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import me.darkovrbaski.items.marketplace.model.QuantityType;

@Value
public class ArticleDto {

  Long id;

  @NotBlank(message = "Name is mandatory")
  String name;

  String description;

  String image;

  QuantityType quantityType;

}
