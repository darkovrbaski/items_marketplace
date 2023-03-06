package me.darkovrbaski.items.marketplace.mapper;

import me.darkovrbaski.items.marketplace.config.CentralMapperConfig;
import me.darkovrbaski.items.marketplace.dto.MoneyDto;
import me.darkovrbaski.items.marketplace.dto.WalletDto;
import me.darkovrbaski.items.marketplace.model.Money;
import me.darkovrbaski.items.marketplace.model.Wallet;
import me.darkovrbaski.items.marketplace.service.intefaces.ImageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = CentralMapperConfig.class)
public abstract class WalletMapper {

  @Autowired
  ImageService imageService;

  public abstract Money toEntity(MoneyDto money);

  public abstract MoneyDto toDto(Money money);

  @Mapping(
      target = "user.image",
      expression = "java(imageService.getSignedImageUrl(user.getImage()))"
  )
  public abstract WalletDto toDto(Wallet wallet);

}
