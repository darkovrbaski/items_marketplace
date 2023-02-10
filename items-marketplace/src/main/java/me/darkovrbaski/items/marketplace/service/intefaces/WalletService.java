package me.darkovrbaski.items.marketplace.service.intefaces;

import me.darkovrbaski.items.marketplace.dto.MoneyDto;
import me.darkovrbaski.items.marketplace.dto.WalletDto;
import me.darkovrbaski.items.marketplace.model.User;

public interface WalletService {

  void createWallet(User user);

  WalletDto getWallet(Long userId);

  WalletDto addFunds(Long userId, MoneyDto amount);

  WalletDto spendFunds(Long userId, MoneyDto amount);

}
