package me.darkovrbaski.items.marketplace.repository;

import java.math.BigDecimal;
import java.util.List;
import me.darkovrbaski.items.marketplace.model.Order;
import me.darkovrbaski.items.marketplace.model.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends EntityRepository<Order> {

  @Query(
      "SELECT o FROM Order o "
          + "WHERE o.status LIKE 'OPEN' AND o.user.id = :userId "
  )
  List<Order> findAllOpenOrdersByUserId(@Param("userId") Long userId);

  Page<Order> findAllByUserId(Long userId, Pageable pageable);

  @Query(
      "SELECT o FROM Order o "
          + "WHERE o.status LIKE 'OPEN' AND o.type LIKE 'SELL' AND o.price.amount <= :price "
          + "ORDER BY o.price.amount ASC, o.createdDateTime ASC"
  )
  List<Order> getOpenSellLowerPricedOlderOrders(@Param("price") BigDecimal price);

  @Query(
      "SELECT o FROM Order o "
          + "WHERE o.status LIKE 'OPEN' AND o.type LIKE 'BUY' AND o.price.amount >= :price "
          + "ORDER BY o.price.amount DESC, o.createdDateTime ASC"
  )
  List<Order> getOpenBuyHigherPricedOlderOrders(@Param("price") BigDecimal price);


  @Query(
      "SELECT o FROM Order o "
          + "WHERE o.status LIKE 'OPEN' AND o.type LIKE :type AND o.article.id = :articleId "
  )
  List<Order> getOpenBuyOrdersByArticleIdAndType(@Param("articleId") Long articleId,
      @Param("type") OrderType type);

}
