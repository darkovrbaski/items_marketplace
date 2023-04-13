package me.darkovrbaski.items.marketplace.mapper;

import me.darkovrbaski.items.marketplace.config.CentralMapperConfig;
import me.darkovrbaski.items.marketplace.dto.ArticleItemDto;
import me.darkovrbaski.items.marketplace.model.ArticleItem;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class, uses = {ArticleMapper.class})
public interface ArticleItemMapper {

  ArticleItemDto toDto(ArticleItem articleItem);

}
