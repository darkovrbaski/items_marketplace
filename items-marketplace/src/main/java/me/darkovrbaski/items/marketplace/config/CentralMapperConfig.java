package me.darkovrbaski.items.marketplace.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CentralMapperConfig {

}
