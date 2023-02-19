package me.darkovrbaski.items.marketplace.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.OrderDto;
import me.darkovrbaski.items.marketplace.mapper.OrderMapper;
import me.darkovrbaski.items.marketplace.model.Money;
import me.darkovrbaski.items.marketplace.model.Order;
import me.darkovrbaski.items.marketplace.model.OrderStatus;
import me.darkovrbaski.items.marketplace.model.OrderType;
import me.darkovrbaski.items.marketplace.model.Trade;
import me.darkovrbaski.items.marketplace.repository.OrderRepository;
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

  final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

  OrderServiceImpl orderService;

  @BeforeEach
  void setUp() {
    orderService = new OrderServiceImpl(orderRepository, orderMapper, tradeService,
        inventoryService, walletService);
  }

  @Test
  void givenBuyOrderWithEqualQuantity_whenCreateOrder_thenClosedOrderIsCreated() {
    final Order buyOrder = Order.builder()
        .status(OrderStatus.OPEN)
        .type(OrderType.BUY)
        .price(Money.dollars(BigDecimal.valueOf(10)))
        .quantity(BigDecimal.valueOf(30))
        .filledQuantity(BigDecimal.ZERO)
        .build();
    final OrderDto buyOrderDto = orderMapper.toDto(buyOrder);
    final List<Order> openSellOrders = new ArrayList<>() {
      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.SELL)
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(20))
            .filledQuantity(BigDecimal.ZERO).build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.SELL)
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(7))
            .filledQuantity(BigDecimal.ZERO).build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.SELL)
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(3))
            .filledQuantity(BigDecimal.ZERO).build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.SELL)
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(3))
            .filledQuantity(BigDecimal.ZERO).build()
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
        .build();
    final OrderDto expectedBuyOrderDto = orderMapper.toDto(expectedBuyOrder);

    when(orderRepository.getOpenSellLowerPricedOlderOrders(any())).thenReturn(openSellOrders);
    when(orderRepository.save(any(Order.class))).thenReturn(buyOrder);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any()))
        .thenReturn(trades.get(0), trades.get(1), trades.get(2));

    final var resultOrder = orderService.createOrder(buyOrderDto);

    assertThat(resultOrder).isEqualTo(expectedBuyOrderDto);
  }

  @Test
  void givenBuyOrderWithSurplusQuantity_whenCreateOrder_thenOpenOrderIsCreated() {
    final Order buyOrder = Order.builder()
        .status(OrderStatus.OPEN)
        .type(OrderType.BUY)
        .price(Money.dollars(BigDecimal.valueOf(10)))
        .quantity(BigDecimal.valueOf(30))
        .filledQuantity(BigDecimal.ZERO)
        .build();
    final OrderDto buyOrderDto = orderMapper.toDto(buyOrder);
    final List<Order> openSellOrders = new ArrayList<>() {
      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.SELL)
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(7))
            .filledQuantity(BigDecimal.ZERO).build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.SELL)
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(3))
            .filledQuantity(BigDecimal.ZERO).build()
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
        .build();
    final OrderDto expectedBuyOrderDto = orderMapper.toDto(expectedBuyOrder);

    when(orderRepository.getOpenSellLowerPricedOlderOrders(any())).thenReturn(openSellOrders);
    when(orderRepository.save(any(Order.class))).thenReturn(buyOrder);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any())).thenReturn(trades.get(0), trades.get(1));

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
        .build();
    final OrderDto buyOrderDto = orderMapper.toDto(buyOrder);
    final List<Order> openSellOrders = new ArrayList<>() {
      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.SELL)
            .price(Money.dollars(BigDecimal.valueOf(8)))
            .quantity(BigDecimal.valueOf(40))
            .filledQuantity(BigDecimal.ZERO).build()
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
        .build();
    final OrderDto expectedBuyOrderDto = orderMapper.toDto(expectedBuyOrder);

    when(orderRepository.getOpenSellLowerPricedOlderOrders(any())).thenReturn(openSellOrders);
    when(orderRepository.save(any(Order.class))).thenReturn(buyOrder);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any())).thenReturn(trades.get(0));

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
        .build();
    final OrderDto buyOrderDto = orderMapper.toDto(buyOrder);
    final List<Order> openBuyOrders = new ArrayList<>() {
      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(15)))
            .quantity(BigDecimal.valueOf(20))
            .filledQuantity(BigDecimal.ZERO).build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(7))
            .filledQuantity(BigDecimal.ZERO).build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(3))
            .filledQuantity(BigDecimal.ZERO).build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(3))
            .filledQuantity(BigDecimal.ZERO).build()
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
        .build();
    final OrderDto expectedBuyOrderDto = orderMapper.toDto(expectedBuyOrder);

    when(orderRepository.getOpenBuyHigherPricedOlderOrders(any())).thenReturn(openBuyOrders);
    when(orderRepository.save(any(Order.class))).thenReturn(buyOrder);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any()))
        .thenReturn(trades.get(0), trades.get(1), trades.get(2));

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
        .build();
    final OrderDto buyOrderDto = orderMapper.toDto(buyOrder);
    final List<Order> openBuyOrders = new ArrayList<>() {
      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(10)))
            .quantity(BigDecimal.valueOf(7))
            .filledQuantity(BigDecimal.ZERO).build()
        );
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(15)))
            .quantity(BigDecimal.valueOf(3))
            .filledQuantity(BigDecimal.ZERO).build()
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
        .build();
    final OrderDto expectedBuyOrderDto = orderMapper.toDto(expectedBuyOrder);

    when(orderRepository.getOpenBuyHigherPricedOlderOrders(any())).thenReturn(openBuyOrders);
    when(orderRepository.save(any(Order.class))).thenReturn(buyOrder);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any())).thenReturn(trades.get(0), trades.get(1));

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
        .build();
    final OrderDto buyOrderDto = orderMapper.toDto(buyOrder);
    final List<Order> openBuyOrders = new ArrayList<>() {
      {
        add(Order.builder()
            .status(OrderStatus.OPEN)
            .type(OrderType.BUY)
            .price(Money.dollars(BigDecimal.valueOf(12)))
            .quantity(BigDecimal.valueOf(40))
            .filledQuantity(BigDecimal.ZERO).build()
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
        .build();
    final OrderDto expectedBuyOrderDto = orderMapper.toDto(expectedBuyOrder);

    when(orderRepository.getOpenBuyHigherPricedOlderOrders(any())).thenReturn(openBuyOrders);
    when(orderRepository.save(any(Order.class))).thenReturn(buyOrder);
    when(orderRepository.saveAll(any())).thenReturn(null);
    when(tradeService.createTrade(any(), any(), any())).thenReturn(trades.get(0));

    final var resultOrder = orderService.createOrder(buyOrderDto);

    assertThat(resultOrder).isEqualTo(expectedBuyOrderDto);
  }
}

