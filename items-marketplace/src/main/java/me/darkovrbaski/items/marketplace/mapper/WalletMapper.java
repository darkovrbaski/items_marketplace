package me.darkovrbaski.items.marketplace.mapper;

import me.darkovrbaski.items.marketplace.dto.MoneyDto;
import me.darkovrbaski.items.marketplace.dto.WalletDto;
import me.darkovrbaski.items.marketplace.model.Money;
import me.darkovrbaski.items.marketplace.model.Wallet;
import org.mapstruct.Mapper;

@Mapper
public interface WalletMapper {

  Money toEntity(MoneyDto money);

  MoneyDto toDto(Money money);
  
  WalletDto toDto(Wallet wallet);

}
