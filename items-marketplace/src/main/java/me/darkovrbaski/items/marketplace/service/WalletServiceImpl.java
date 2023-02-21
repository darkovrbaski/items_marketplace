package me.darkovrbaski.items.marketplace.service;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import me.darkovrbaski.items.marketplace.dto.MoneyDto;
import me.darkovrbaski.items.marketplace.dto.WalletDto;
import me.darkovrbaski.items.marketplace.mapper.WalletMapper;
import me.darkovrbaski.items.marketplace.model.Money;
import me.darkovrbaski.items.marketplace.model.User;
import me.darkovrbaski.items.marketplace.model.Wallet;
import me.darkovrbaski.items.marketplace.repository.WalletRepository;
import me.darkovrbaski.items.marketplace.service.intefaces.WalletService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class WalletServiceImpl implements WalletService {

  WalletRepository walletRepository;
  WalletMapper walletMapper;

  @Override
  public void createWallet(final User user) {
    walletRepository.save(new Wallet(Money.dollars(BigDecimal.ZERO), user));
  }

  @Override
  public WalletDto getWallet(final Long userId) {
    return walletMapper.toDto(walletRepository.findByIdOrThrow(userId));
  }

  @Override
  public WalletDto addFunds(final Long userId, final MoneyDto amount) {
    final var wallet = walletRepository.findByIdOrThrow(userId);
    wallet.addFunds(walletMapper.toEntity(amount));
    return walletMapper.toDto(walletRepository.save(wallet));
  }

  @Override
  public WalletDto spendFunds(final Long userId, final MoneyDto amount) {
    final var wallet = walletRepository.findByIdOrThrow(userId);
    final var a = walletMapper.toEntity(amount);
    wallet.spendFunds(a);
    return walletMapper.toDto(walletRepository.save(wallet));
  }

}
