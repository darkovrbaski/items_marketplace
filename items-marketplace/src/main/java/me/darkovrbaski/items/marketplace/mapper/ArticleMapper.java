package me.darkovrbaski.items.marketplace.mapper;

import me.darkovrbaski.items.marketplace.config.CentralMapperConfig;
import me.darkovrbaski.items.marketplace.dto.ArticleDto;
import me.darkovrbaski.items.marketplace.model.Article;
import me.darkovrbaski.items.marketplace.service.intefaces.ImageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = CentralMapperConfig.class)
public abstract class ArticleMapper {

  @Autowired
  ImageService imageService;

  @Mapping(
      target = "image",
      expression = "java(imageService.getPublicImageUrl(article.getImage()))"
  )
  public abstract ArticleDto toDto(Article article);

  public abstract Article toEntity(ArticleDto articleDto);

}
