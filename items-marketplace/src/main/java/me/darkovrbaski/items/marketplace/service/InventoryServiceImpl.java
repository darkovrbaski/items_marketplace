package me.darkovrbaski.items.marketplace.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.ArticleItemDto;
import me.darkovrbaski.items.marketplace.mapper.ArticleItemMapper;
import me.darkovrbaski.items.marketplace.model.ArticleItem;
import me.darkovrbaski.items.marketplace.model.ArticleTrade;
import me.darkovrbaski.items.marketplace.model.Inventory;
import me.darkovrbaski.items.marketplace.model.User;
import me.darkovrbaski.items.marketplace.repository.InventoryRepository;
import me.darkovrbaski.items.marketplace.service.intefaces.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InventoryServiceImpl implements InventoryService {

  InventoryRepository inventoryRepository;
  ArticleItemMapper articleItemMapper;
  EntityManager em;

  static String ID = "id";
  static String NAME = "name";
  static String ARTICLE_ITEMS = "articleItems";
  static String ARTICLE = "article";

  @Override
  public void createInventory(final User user) {
    inventoryRepository.save(new Inventory(user, new HashSet<>()));
  }

  @Transactional
  @Override
  public Page<ArticleItemDto> getInventory(final Long userId, final int page, final int size) {
    final CriteriaBuilder cb = em.getCriteriaBuilder();
    final CriteriaQuery<ArticleItem> cq = cb.createQuery(ArticleItem.class);
    final Root<Inventory> inventory = cq.from(Inventory.class);

    cq.select(inventory.get(ARTICLE_ITEMS)).where(cb.equal(inventory.get(ID), userId));

    return getArticleItemDtoPages(page, size, cq);
  }

  @Transactional
  @Override
  public Page<ArticleItemDto> searchInventory(final Long userId, final String name, final int page,
      final int size) {
    final CriteriaBuilder cb = em.getCriteriaBuilder();
    final CriteriaQuery<ArticleItem> cq = cb.createQuery(ArticleItem.class);
    final Root<Inventory> inventory = cq.from(Inventory.class);

    cq.select(inventory.get(ARTICLE_ITEMS))
        .where(
            cb.and(
                cb.equal(inventory.get(ID), userId),
                cb.like(inventory.get(ARTICLE_ITEMS).get(ARTICLE).get(NAME), "%" + name + "%")
            )
        );

    return getArticleItemDtoPages(page, size, cq);
  }

  private Page<ArticleItemDto> getArticleItemDtoPages(final int page, final int size,
      final CriteriaQuery<ArticleItem> cq) {
    final TypedQuery<ArticleItem> query = em.createQuery(cq);
    query.setFirstResult(page * size);
    query.setMaxResults(size);

    return new PageImpl<>(
        query.getResultList().stream().map(articleItemMapper::toDto).toList(),
        Pageable.ofSize(size).withPage(page), query.getResultList().size());
  }

  @Override
  public void updateItemInInventory(final Long userId, final ArticleTrade articleTrade) {
    final Inventory inventory = inventoryRepository.findByIdOrThrow(userId);
    inventory.updateArticleItem(articleTrade);
    inventoryRepository.save(inventory);
  }

}
