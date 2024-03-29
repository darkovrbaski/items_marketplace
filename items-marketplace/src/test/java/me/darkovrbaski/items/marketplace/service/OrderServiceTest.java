package me.darkovrbaski.items.marketplace.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.SneakyThrows;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  Clock clock;

  @Autowired
  OrderMapper orderMapper;

  OrderServiceImpl orderService;

  User user;

  Article article;

  @SneakyThrows
  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    article = new Article("Article", null, null);
    article.setId(1L);
    orderService = new OrderServiceImpl(orderRepository, orderMapper, tradeService,
        inventoryService, walletService, articleRepository, userRepository, clock);
  }

  @Test
  void givenBuyOrderWithEqualQuantity_whenCreateOrder_thenClosedOrderIsCreated() {
    final Order order = Order.builder()
        .status(OrderStatus.OPEN)
        .type(OrderType.BUY)
        .price(Money.dollars(BigDecimal.valueOf(10)))
        .quantity(BigDecimal.valueOf(30))
        .filledQuantity(BigDecimal.ZERO)
        .user(user)
        .article(article)
        .enabledAutoTrade(true)
        .build();
    final OrderDto orderDto = orderMapper.toDto(order);
    final List<Order> openSellOrders = new ArrayList<>() {
      @Serial
      private static final long serialVersionUID = -6352716186275622268L;

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
      @Serial
      private static final long serialVersionUID = 6049392333647917250L;

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
    final Order expectedOrder = Order.builder()
        .id(0L)
        .createdDateTime(LocalDateTime.now(clock))
        .type(order.getType())
        .price(order.getPrice())
        .quantity(order.getQuantity())
        .filledQuantity(BigDecimal.valueOf(30))
        .trades(trades)
        .status(OrderStatus.CLOSED)
        .user(user)
        .article(article)
        .enabledAutoTrade(true)
        .lowerSellPrice(Money.dollars(BigDecimal.ZERO))
        .build();
    final OrderDto expectedOrderDto = orderMapper.toDto(expectedOrder);

    when(orderRepository.getOpenSellLowerPricedOlderOrders(any(), any())).thenReturn(
        openSellOrders);
    when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
    when(tradeService.createTrade(any(), any(), any()))
        .thenReturn(trades.get(0), trades.get(1), trades.get(2));
    redundantStubs();

    final var resultOrder = orderService.createOrder(orderDto);

    assertThat(resultOrder).isEqualTo(expectedOrderDto);
  }

  private void redundantStubs() {
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(articleRepository.findByIdOrThrow(anyLong())).thenReturn(article);
    when(userRepository.findByIdOrThrow(anyLong())).thenReturn(user);
    when(walletService.spendFunds(any(), any())).thenReturn(null);
    when(walletService.addFunds(any(), any())).thenReturn(null);
    when(walletService.getWallet(any())).thenReturn(
        new WalletDto(new MoneyDto(BigDecimal.valueOf(1000), "USD"), null));
    when(inventoryService.searchInventory(any(), anyString(), anyInt(), anyInt())).thenReturn(
        new PageImpl<>(new ArrayList<>() {
          @Serial
          private static final long serialVersionUID = -2686540700166875564L;

          {
            add(new ArticleItemDto(BigDecimal.valueOf(1000), null));
          }
        })
    );
  }

  @Test
  void givenBuyOrderWithSurplusQuantity_whenCreateOrder_thenOpenOrderIsCreated() {
    final Order order = Order.builder()
        .status(OrderStatus.OPEN)
        .type(OrderType.BUY)
        .price(Money.dollars(BigDecimal.valueOf(10)))
        .quantity(BigDecimal.valueOf(30))
        .filledQuantity(BigDecimal.ZERO)
        .user(user)
        .article(article)
        .enabledAutoTrade(true)
        .build();
    final OrderDto orderDto = orderMapper.toDto(order);
    final List<Order> openSellOrders = new ArrayList<>() {
      @Serial
      private static final long serialVersionUID = 5320705315099411443L;

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
      @Serial
      private static final long serialVersionUID = -4294044600267205571L;

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
    final Order expectedOrder = Order.builder()
        .id(0L)
        .createdDateTime(LocalDateTime.now(clock))
        .type(order.getType())
        .price(order.getPrice())
        .quantity(order.getQuantity())
        .filledQuantity(BigDecimal.valueOf(10))
        .trades(trades)
        .status(OrderStatus.OPEN)
        .user(user)
        .article(article)
        .enabledAutoTrade(true)
        .lowerSellPrice(Money.dollars(BigDecimal.ZERO))
        .build();
    final OrderDto expectedOrderDto = orderMapper.toDto(expectedOrder);

    when(orderRepository.getOpenSellLowerPricedOlderOrders(any(), any())).thenReturn(
        openSellOrders);
    when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any())).thenReturn(trades.get(0), trades.get(1));
    redundantStubs();

    final var resultOrder = orderService.createOrder(orderDto);

    assertThat(resultOrder).isEqualTo(expectedOrderDto);
  }

  @Test
  void givenBuyOrderWithDeficitQuantity_whenCreateOrder_thenClosedOrderIsCreated() {
    final Order order = Order.builder()
        .status(OrderStatus.OPEN)
        .type(OrderType.BUY)
        .price(Money.dollars(BigDecimal.valueOf(10)))
        .quantity(BigDecimal.valueOf(30))
        .filledQuantity(BigDecimal.ZERO)
        .user(user)
        .article(article)
        .enabledAutoTrade(true)
        .build();
    final OrderDto orderDto = orderMapper.toDto(order);
    final List<Order> openSellOrders = new ArrayList<>() {
      @Serial
      private static final long serialVersionUID = 8049257365807463280L;

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
      @Serial
      private static final long serialVersionUID = 4559578227184386785L;

      {
        add(Trade.builder()
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(30))
            .build()
        );
      }
    };
    final Order expectedOrder = Order.builder()
        .id(0L)
        .createdDateTime(LocalDateTime.now(clock))
        .type(order.getType())
        .price(order.getPrice())
        .quantity(order.getQuantity())
        .filledQuantity(order.getQuantity())
        .trades(trades)
        .status(OrderStatus.CLOSED)
        .user(user)
        .article(article)
        .enabledAutoTrade(true)
        .lowerSellPrice(Money.dollars(BigDecimal.ZERO))
        .build();
    final OrderDto expectedOrderDto = orderMapper.toDto(expectedOrder);

    when(orderRepository.getOpenSellLowerPricedOlderOrders(any(), any())).thenReturn(
        openSellOrders);
    when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any())).thenReturn(trades.get(0));
    redundantStubs();

    final var resultOrder = orderService.createOrder(orderDto);

    assertThat(resultOrder).isEqualTo(expectedOrderDto);
  }

  @Test
  void givenSellOrderWithEqualQuantity_whenCreateOrder_thenClosedOrderIsCreated() {
    final Order order = Order.builder()
        .status(OrderStatus.OPEN)
        .type(OrderType.SELL)
        .price(Money.dollars(BigDecimal.valueOf(10)))
        .quantity(BigDecimal.valueOf(30))
        .filledQuantity(BigDecimal.ZERO)
        .user(user)
        .article(article)
        .lowerSellPrice(Money.dollars(BigDecimal.ZERO))
        .build();
    final OrderDto orderDto = orderMapper.toDto(order);
    final List<Order> openBuyOrders = new ArrayList<>() {
      @Serial
      private static final long serialVersionUID = 8468308233928102888L;

      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(15)))
            .quantity(BigDecimal.valueOf(20))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .enabledAutoTrade(true)
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
            .enabledAutoTrade(true)
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
            .enabledAutoTrade(true)
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
            .enabledAutoTrade(true)
            .build()
        );
      }
    };
    final List<Trade> trades = new ArrayList<>() {
      @Serial
      private static final long serialVersionUID = -414446687789826305L;

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
    final Order expectedOrder = Order.builder()
        .id(0L)
        .createdDateTime(LocalDateTime.now(clock))
        .type(order.getType())
        .price(order.getPrice())
        .quantity(order.getQuantity())
        .filledQuantity(BigDecimal.valueOf(30))
        .trades(trades)
        .status(OrderStatus.CLOSED)
        .user(user)
        .article(article)
        .enabledAutoTrade(true)
        .lowerSellPrice(order.getPrice())
        .build();
    final OrderDto expectedOrderDto = orderMapper.toDto(expectedOrder);

    when(orderRepository.getOpenBuyHigherPricedOlderOrders(any(), any())).thenReturn(openBuyOrders);
    when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any()))
        .thenReturn(trades.get(0), trades.get(1), trades.get(2));
    redundantStubs();

    final var resultOrder = orderService.createOrder(orderDto);

    assertThat(resultOrder).isEqualTo(expectedOrderDto);
  }

  @Test
  void givenSellOrderWithSurplusQuantity_whenCreateOrder_thenOpenOrderIsCreated() {
    final Order order = Order.builder()
        .status(OrderStatus.OPEN)
        .type(OrderType.SELL)
        .price(Money.dollars(BigDecimal.valueOf(10)))
        .quantity(BigDecimal.valueOf(30))
        .filledQuantity(BigDecimal.ZERO)
        .user(user)
        .article(article)
        .lowerSellPrice(Money.dollars(BigDecimal.ZERO))
        .build();
    final OrderDto orderDto = orderMapper.toDto(order);
    final List<Order> openBuyOrders = new ArrayList<>() {
      @Serial
      private static final long serialVersionUID = -8561555535360685702L;

      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(7))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .enabledAutoTrade(true)
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
            .enabledAutoTrade(true)
            .build()
        );
      }
    };
    final List<Trade> trades = new ArrayList<>() {
      @Serial
      private static final long serialVersionUID = -3029068383132419802L;

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
    final Order expectedOrder = Order.builder()
        .id(0L)
        .createdDateTime(LocalDateTime.now(clock))
        .type(order.getType())
        .price(order.getPrice())
        .quantity(order.getQuantity())
        .filledQuantity(BigDecimal.valueOf(10))
        .trades(trades)
        .status(OrderStatus.OPEN)
        .user(user)
        .article(article)
        .enabledAutoTrade(true)
        .lowerSellPrice(order.getPrice())
        .build();
    final OrderDto expectedOrderDto = orderMapper.toDto(expectedOrder);

    when(orderRepository.getOpenBuyHigherPricedOlderOrders(any(), any())).thenReturn(openBuyOrders);
    when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any())).thenReturn(trades.get(0), trades.get(1));
    redundantStubs();

    final var resultOrder = orderService.createOrder(orderDto);

    assertThat(resultOrder).isEqualTo(expectedOrderDto);
  }

  @Test
  void givenSellOrderWithDeficitQuantity_whenCreateOrder_thenClosedOrderIsCreated() {
    final Order order = Order.builder()
        .status(OrderStatus.OPEN)
        .type(OrderType.SELL)
        .price(Money.dollars(BigDecimal.valueOf(10)))
        .quantity(BigDecimal.valueOf(30))
        .filledQuantity(BigDecimal.ZERO)
        .user(user)
        .article(article)
        .lowerSellPrice(Money.dollars(BigDecimal.ZERO))
        .build();
    final OrderDto orderDto = orderMapper.toDto(order);
    final List<Order> openBuyOrders = new ArrayList<>() {
      @Serial
      private static final long serialVersionUID = -854986627802858128L;

      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(12)))
            .quantity(BigDecimal.valueOf(40))
            .filledQuantity(BigDecimal.ZERO)
            .user(user)
            .article(article)
            .enabledAutoTrade(true)
            .build()
        );
      }
    };
    final List<Trade> trades = new ArrayList<>() {
      @Serial
      private static final long serialVersionUID = -4938134031366128700L;

      {
        add(Trade.builder()
            .price(Money.dollars(BigDecimal.valueOf(12)))
            .quantity(BigDecimal.valueOf(30))
            .build()
        );
      }
    };
    final Order expectedOrder = Order.builder()
        .id(0L)
        .createdDateTime(LocalDateTime.now(clock))
        .type(order.getType())
        .price(order.getPrice())
        .quantity(order.getQuantity())
        .filledQuantity(order.getQuantity())
        .trades(trades)
        .status(OrderStatus.CLOSED)
        .user(user)
        .article(article)
        .enabledAutoTrade(true)
        .lowerSellPrice(order.getPrice())
        .build();
    final OrderDto expectedOrderDto = orderMapper.toDto(expectedOrder);

    when(orderRepository.getOpenBuyHigherPricedOlderOrders(any(), any())).thenReturn(openBuyOrders);
    when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any())).thenReturn(trades.get(0));
    redundantStubs();

    final var resultOrder = orderService.createOrder(orderDto);

    assertThat(resultOrder).isEqualTo(expectedOrderDto);
  }
}

