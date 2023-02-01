package me.darkovrbaski.items.marketplace.repository;

import me.darkovrbaski.items.marketplace.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends EntityRepository<Article> {

  boolean existsByName(String name);

  Page<Article> findByNameContainsIgnoreCase(String name, Pageable pageable);
}
