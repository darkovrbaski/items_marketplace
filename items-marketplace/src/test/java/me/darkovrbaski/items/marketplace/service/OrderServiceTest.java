package me.darkovrbaski.items.marketplace.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.ArticleItemDto;
import me.darkovrbaski.items.marketplace.dto.MoneyDto;
import me.darkovrbaski.items.marketplace.dto.OrderDto;
import me.darkovrbaski.items.marketplace.dto.WalletDto;
import me.darkovrbaski.items.marketplace.mapper.OrderMapper;
import me.darkovrbaski.items.marketplace.model.Article;
import me.darkovrbaski.items.marketplace.model.Money;
import me.darkovrbaski.items.marketplace.model.Order;
import me.darkovrbaski.items.marketplace.model.OrderStatus;
import me.darkovrbaski.items.marketplace.model.OrderType;
import me.darkovrbaski.items.marketplace.model.Trade;
import me.darkovrbaski.items.marketplace.model.User;
import me.darkovrbaski.items.marketplace.repository.ArticleRepository;
import me.darkovrbaski.items.marketplace.repository.OrderRepository;
import me.darkovrbaski.items.marketplace.repository.UserRepository;
import me.darkovrbaski.items.marketplace.service.intefaces.InventoryService;
import me.darkovrbaski.items.marketplace.service.intefaces.TradeService;
import me.darkovrbaski.items.marketplace.service.intefaces.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)
class OrderServiceTest {

  @Mock
  OrderRepository orderRepository;

  @Mock
  TradeService tradeService;

  @Mock
  InventoryService inventoryService;

  @Mock
  WalletService walletService;

  @Mock
  ArticleRepository articleRepository;

  @Mock
  UserRepository userRepository;

  final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

  OrderServiceImpl orderService;

  final User user = new User();

  final Article article = new Article("Article", null, null);

  @BeforeEach
  void setUp() {
    user.setId(1L);
    orderService = new OrderServiceImpl(orderRepository, orderMapper, tradeService,
        inventoryService, walletService, articleRepository, userRepository);
  }

  @Test
  void givenBuyOrderWithEqualQuantity_whenCreateOrder_thenClosedOrderIsCreated() {
    final Order buyOrder = Order.builder()
        .status(OrderStatus.OPEN)
        .type(OrderType.BUY)
        .price(Money.dollars(BigDecimal.valueOf(10)))
        .quantity(BigDecimal.valueOf(30))
        .filledQuantity(BigDecimal.ZERO)
        .user(user)
        .article(article)
        .build();
    final OrderDto buyOrderDto = orderMapper.toDto(buyOrder);
    final List<Order> openSellOrders = new ArrayList<>() {
      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.SELL)
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(20))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.SELL)
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(7))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.SELL)
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(3))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.SELL)
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(3))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .build()
        );
      }
    };
    final List<Trade> trades = new ArrayList<>() {
      {
        add(Trade.builder()
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(7))
            .build()
        );
        add(Trade.builder()
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(3))
            .build()
        );
        add(Trade.builder()
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(20))
            .build()
        );
      }
    };
    final Order expectedBuyOrder = Order.builder()
        .type(buyOrder.getType())
        .price(buyOrder.getPrice())
        .quantity(buyOrder.getQuantity())
        .filledQuantity(BigDecimal.valueOf(30))
        .trades(trades)
        .status(OrderStatus.CLOSED)
        .user(user)
        .article(article)
        .build();
    final OrderDto expectedBuyOrderDto = orderMapper.toDto(expectedBuyOrder);

    when(orderRepository.getOpenSellLowerPricedOlderOrders(any())).thenReturn(openSellOrders);
    when(orderRepository.save(any(Order.class))).thenReturn(buyOrder);
    when(tradeService.createTrade(any(), any(), any()))
        .thenReturn(trades.get(0), trades.get(1), trades.get(2));
    when(walletService.getWallet(any())).thenReturn(
        new WalletDto(new MoneyDto(BigDecimal.valueOf(1000), "USD"), null));
    redundantStubs();

    final var resultOrder = orderService.createOrder(buyOrderDto);

    assertThat(resultOrder).isEqualTo(expectedBuyOrderDto);
  }

  private void redundantStubs() {
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(walletService.spendFunds(any(), any())).thenReturn(null);
    when(walletService.addFunds(any(), any())).thenReturn(null);
  }

  @Test
  void givenBuyOrderWithSurplusQuantity_whenCreateOrder_thenOpenOrderIsCreated() {
    final Order buyOrder = Order.builder()
        .status(OrderStatus.OPEN)
        .type(OrderType.BUY)
        .price(Money.dollars(BigDecimal.valueOf(10)))
        .quantity(BigDecimal.valueOf(30))
        .filledQuantity(BigDecimal.ZERO)
        .user(user)
        .article(article)
        .build();
    final OrderDto buyOrderDto = orderMapper.toDto(buyOrder);
    final List<Order> openSellOrders = new ArrayList<>() {
      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.SELL)
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(7))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.SELL)
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(3))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .build()
        );
      }
    };
    final List<Trade> trades = new ArrayList<>() {
      {
        add(Trade.builder()
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(7))
            .build()
        );
        add(Trade.builder()
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(3))
            .build()
        );
      }
    };
    final Order expectedBuyOrder = Order.builder()
        .type(buyOrder.getType())
        .price(buyOrder.getPrice())
        .quantity(buyOrder.getQuantity())
        .filledQuantity(BigDecimal.valueOf(10))
        .trades(trades)
        .status(OrderStatus.OPEN)
        .user(user)
        .article(article)
        .build();
    final OrderDto expectedBuyOrderDto = orderMapper.toDto(expectedBuyOrder);

    when(orderRepository.getOpenSellLowerPricedOlderOrders(any())).thenReturn(openSellOrders);
    when(orderRepository.save(any(Order.class))).thenReturn(buyOrder);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any())).thenReturn(trades.get(0), trades.get(1));
    when(walletService.getWallet(any())).thenReturn(
        new WalletDto(new MoneyDto(BigDecimal.valueOf(1000), "USD"), null));
    redundantStubs();

    final var resultOrder = orderService.createOrder(buyOrderDto);

    assertThat(resultOrder).isEqualTo(expectedBuyOrderDto);
  }

  @Test
  void givenBuyOrderWithDeficitQuantity_whenCreateOrder_thenClosedOrderIsCreated() {
    final Order buyOrder = Order.builder()
        .status(OrderStatus.OPEN)
        .type(OrderType.BUY)
        .price(Money.dollars(BigDecimal.valueOf(10)))
        .quantity(BigDecimal.valueOf(30))
        .filledQuantity(BigDecimal.ZERO)
        .user(user)
        .article(article)
        .build();
    final OrderDto buyOrderDto = orderMapper.toDto(buyOrder);
    final List<Order> openSellOrders = new ArrayList<>() {
      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.SELL)
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(40))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .build()
        );
      }
    };
    final List<Trade> trades = new ArrayList<>() {
      {
        add(Trade.builder()
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(30))
            .build()
        );
      }
    };
    final Order expectedBuyOrder = Order.builder()
        .type(buyOrder.getType())
        .price(buyOrder.getPrice())
        .quantity(buyOrder.getQuantity())
        .filledQuantity(buyOrder.getQuantity())
        .trades(trades)
        .status(OrderStatus.CLOSED)
        .user(user)
        .article(article)
        .build();
    final OrderDto expectedBuyOrderDto = orderMapper.toDto(expectedBuyOrder);

    when(orderRepository.getOpenSellLowerPricedOlderOrders(any())).thenReturn(openSellOrders);
    when(orderRepository.save(any(Order.class))).thenReturn(buyOrder);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any())).thenReturn(trades.get(0));
    when(walletService.getWallet(any())).thenReturn(
        new WalletDto(new MoneyDto(BigDecimal.valueOf(1000), "USD"), null));
    redundantStubs();

    final var resultOrder = orderService.createOrder(buyOrderDto);

    assertThat(resultOrder).isEqualTo(expectedBuyOrderDto);
  }

  @Test
  void givenSellOrderWithEqualQuantity_whenCreateOrder_thenClosedOrderIsCreated() {
    final Order buyOrder = Order.builder()
        .status(OrderStatus.OPEN)
        .type(OrderType.SELL)
        .price(Money.dollars(BigDecimal.valueOf(10)))
        .quantity(BigDecimal.valueOf(30))
        .filledQuantity(BigDecimal.ZERO)
        .user(user)
        .article(article)
        .build();
    final OrderDto buyOrderDto = orderMapper.toDto(buyOrder);
    final List<Order> openBuyOrders = new ArrayList<>() {
      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(15)))
            .quantity(BigDecimal.valueOf(20))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(7))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(3))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(3))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .build()
        );
      }
    };
    final List<Trade> trades = new ArrayList<>() {
      {
        add(Trade.builder()
            .price(Money.dollars(BigDecimal.valueOf(15)))
            .quantity(BigDecimal.valueOf(20))
            .build()
        );
        add(Trade.builder()
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(7))
            .build()
        );
        add(Trade.builder()
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(3))
            .build()
        );
      }
    };
    final Order expectedBuyOrder = Order.builder()
        .type(buyOrder.getType())
        .price(buyOrder.getPrice())
        .quantity(buyOrder.getQuantity())
        .filledQuantity(BigDecimal.valueOf(30))
        .trades(trades)
        .status(OrderStatus.CLOSED)
        .user(user)
        .article(article)
        .build();
    final OrderDto expectedBuyOrderDto = orderMapper.toDto(expectedBuyOrder);

    when(orderRepository.getOpenBuyHigherPricedOlderOrders(any())).thenReturn(openBuyOrders);
    when(orderRepository.save(any(Order.class))).thenReturn(buyOrder);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any()))
        .thenReturn(trades.get(0), trades.get(1), trades.get(2));
    when(inventoryService.searchInventory(any(Long.class), any(String.class), any(int.class),
        any(int.class))).thenReturn(
        new PageImpl<>(new ArrayList<>() {
          {
            add(new ArticleItemDto(buyOrder.getQuantity(), buyOrderDto.article()));
          }
        })
    );
    redundantStubs();

    final var resultOrder = orderService.createOrder(buyOrderDto);

    assertThat(resultOrder).isEqualTo(expectedBuyOrderDto);
  }

  @Test
  void givenSellOrderWithSurplusQuantity_whenCreateOrder_thenOpenOrderIsCreated() {
    final Order buyOrder = Order.builder()
        .status(OrderStatus.OPEN)
        .type(OrderType.SELL)
        .price(Money.dollars(BigDecimal.valueOf(10)))
        .quantity(BigDecimal.valueOf(30))
        .filledQuantity(BigDecimal.ZERO)
        .user(user)
        .article(article)
        .build();
    final OrderDto buyOrderDto = orderMapper.toDto(buyOrder);
    final List<Order> openBuyOrders = new ArrayList<>() {
      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(7))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(15)))
            .quantity(BigDecimal.valueOf(3))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .build()
        );
      }
    };
    final List<Trade> trades = new ArrayList<>() {
      {
        add(Trade.builder()
            .price(Money.dollars(BigDecimal.valueOf(15)))
            .quantity(BigDecimal.valueOf(3))
            .build()
        );
        add(Trade.builder()
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(7))
            .build()
        );
      }
    };
    final Order expectedBuyOrder = Order.builder()
        .type(buyOrder.getType())
        .price(buyOrder.getPrice())
        .quantity(buyOrder.getQuantity())
        .filledQuantity(BigDecimal.valueOf(10))
        .trades(trades)
        .status(OrderStatus.OPEN)
        .user(user)
        .article(article)
        .build();
    final OrderDto expectedBuyOrderDto = orderMapper.toDto(expectedBuyOrder);

    when(orderRepository.getOpenBuyHigherPricedOlderOrders(any())).thenReturn(openBuyOrders);
    when(orderRepository.save(any(Order.class))).thenReturn(buyOrder);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any())).thenReturn(trades.get(0), trades.get(1));
    when(inventoryService.searchInventory(any(Long.class), any(String.class), any(int.class),
        any(int.class))).thenReturn(
        new PageImpl<>(new ArrayList<>() {
          {
            add(new ArticleItemDto(buyOrder.getQuantity(), buyOrderDto.article()));
          }
        })
    );
    redundantStubs();

    final var resultOrder = orderService.createOrder(buyOrderDto);

    assertThat(resultOrder).isEqualTo(expectedBuyOrderDto);
  }

  @Test
  void givenSellOrderWithDeficitQuantity_whenCreateOrder_thenClosedOrderIsCreated() {
    final Order buyOrder = Order.builder()
        .status(OrderStatus.OPEN)
        .type(OrderType.SELL)
        .price(Money.dollars(BigDecimal.valueOf(10)))
        .quantity(BigDecimal.valueOf(30))
        .filledQuantity(BigDecimal.ZERO)
        .user(user)
        .article(article)
        .build();
    final OrderDto buyOrderDto = orderMapper.toDto(buyOrder);
    final List<Order> openBuyOrders = new ArrayList<>() {
      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(12)))
            .quantity(BigDecimal.valueOf(40))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .build()
        );
      }
    };
    final List<Trade> trades = new ArrayList<>() {
      {
        add(Trade.builder()
            .price(Money.dollars(BigDecimal.valueOf(12)))
            .quantity(BigDecimal.valueOf(30))
            .build()
        );
      }
    };
    final Order expectedBuyOrder = Order.builder()
        .type(buyOrder.getType())
        .price(buyOrder.getPrice())
        .quantity(buyOrder.getQuantity())
        .filledQuantity(buyOrder.getQuantity())
        .trades(trades)
        .status(OrderStatus.CLOSED)
        .user(user)
        .article(article)
        .build();
    final OrderDto expectedBuyOrderDto = orderMapper.toDto(expectedBuyOrder);

    when(orderRepository.getOpenBuyHigherPricedOlderOrders(any())).thenReturn(openBuyOrders);
    when(orderRepository.save(any(Order.class))).thenReturn(buyOrder);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any())).thenReturn(trades.get(0));
    when(inventoryService.searchInventory(any(Long.class), any(String.class), any(int.class),
        any(int.class))).thenReturn(
        new PageImpl<>(new ArrayList<>() {
          {
            add(new ArticleItemDto(buyOrder.getQuantity(), buyOrderDto.article()));
          }
        })
    );
    redundantStubs();

    final var resultOrder = orderService.createOrder(buyOrderDto);

    assertThat(resultOrder).isEqualTo(expectedBuyOrderDto);
  }
}

