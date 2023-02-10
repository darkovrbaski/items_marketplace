package me.darkovrbaski.items.marketplace.service.intefaces;

import me.darkovrbaski.items.marketplace.dto.ArticleItemDto;
import me.darkovrbaski.items.marketplace.model.ArticleItem;
import me.darkovrbaski.items.marketplace.model.User;
import org.springframework.data.domain.Page;

public interface InventoryService {

  void createInventory(User user);

  Page<ArticleItemDto> getInventory(Long userId, int page, int size);

  Page<ArticleItemDto> searchInventory(Long userId, String name, int page, int size);

  void addItemToInventory(Long userId, ArticleItem articleItem);

  void removeItemFromInventory(Long userId, ArticleItem articleItem);

  void updateItemInInventory(Long userId, ArticleItem articleItem);

}