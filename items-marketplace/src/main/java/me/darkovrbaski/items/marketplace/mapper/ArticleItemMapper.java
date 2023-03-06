package me.darkovrbaski.items.marketplace.mapper;

import me.darkovrbaski.items.marketplace.config.CentralMapperConfig;
import me.darkovrbaski.items.marketplace.dto.ArticleItemDto;
import me.darkovrbaski.items.marketplace.model.ArticleItem;
import me.darkovrbaski.items.marketplace.service.intefaces.ImageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = CentralMapperConfig.class)
public abstract class ArticleItemMapper {

  @Autowired
  ImageService imageService;

  @Mapping(
      target = "article.image",
      expression = "java(imageService.getPublicImageUrl(article.getImage()))"
  )
  public abstract ArticleItemDto toDto(ArticleItem articleItem);

}
