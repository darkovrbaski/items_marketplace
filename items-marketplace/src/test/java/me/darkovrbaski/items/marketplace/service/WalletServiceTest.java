package me.darkovrbaski.items.marketplace.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.MoneyDto;
import me.darkovrbaski.items.marketplace.dto.WalletDto;
import me.darkovrbaski.items.marketplace.mapper.WalletMapper;
import me.darkovrbaski.items.marketplace.model.Money;
import me.darkovrbaski.items.marketplace.model.Wallet;
import me.darkovrbaski.items.marketplace.repository.WalletRepository;
import me.darkovrbaski.items.marketplace.service.intefaces.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)
class WalletServiceTest {

  @Mock
  WalletRepository walletRepository;

  @Autowired
  WalletMapper walletMapper;

  WalletService walletService;

  @BeforeEach
  public void setUp() {
    walletService = new WalletServiceImpl(walletRepository, walletMapper);
  }

  @Test
  void givenAmount_whenAddFunds_thenWalletFundsSummed() {
    final Money amount = Money.dollars(BigDecimal.valueOf(100));
    final MoneyDto amountDto = walletMapper.toDto(amount);
    final Wallet wallet = new Wallet(Money.dollars(BigDecimal.valueOf(300)), null);
    final Wallet expectedWallet = new Wallet(Money.dollars(BigDecimal.valueOf(400)), null);
    final WalletDto expectedWalletDto = walletMapper.toDto(expectedWallet);

    when(walletRepository.findByIdOrThrow(1L)).thenReturn(wallet);
    when(walletRepository.save(wallet)).thenAnswer(i -> i.getArgument(0));

    final var resultWallet = walletService.addFunds(1L, amountDto);

    assertThat(resultWallet).isEqualTo(expectedWalletDto);
  }

  @Test
  void givenAmount_whenSpendFunds_thenWalletFundsSub() {
    final Money amount = Money.dollars(BigDecimal.valueOf(100));
    final MoneyDto amountDto = walletMapper.toDto(amount);
    final Wallet wallet = new Wallet(Money.dollars(BigDecimal.valueOf(300)), null);
    final Wallet expectedWallet = new Wallet(Money.dollars(BigDecimal.valueOf(200)), null);
    final WalletDto expectedWalletDto = walletMapper.toDto(expectedWallet);

    when(walletRepository.findByIdOrThrow(1L)).thenReturn(wallet);
    when(walletRepository.save(wallet)).thenAnswer(i -> i.getArgument(0));

    final var resultWallet = walletService.spendFunds(1L, amountDto);

    assertThat(resultWallet).isEqualTo(expectedWalletDto);
  }

  @Test
  void givenAmount_whenSpendFunds_thenTrowNotEnoughFunds() {
    final Money amount = Money.dollars(BigDecimal.valueOf(100));
    final MoneyDto amountDto = walletMapper.toDto(amount);
    final Wallet wallet = new Wallet(Money.dollars(BigDecimal.valueOf(10)), null);

    when(walletRepository.findByIdOrThrow(1L)).thenReturn(wallet);

    final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      walletService.spendFunds(1L, amountDto);
    });

    assertEquals("Not enough funds", exception.getMessage());
    verify(walletRepository, never()).save(any());
  }
}
