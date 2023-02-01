package me.darkovrbaski.items.marketplace.mapper;

import me.darkovrbaski.items.marketplace.dto.ArticleDto;
import me.darkovrbaski.items.marketplace.model.Article;
import org.mapstruct.Mapper;

@Mapper
public interface ArticleMapper {

  ArticleDto toDto(Article article);

  Article toEntity(ArticleDto articleDto);

}
