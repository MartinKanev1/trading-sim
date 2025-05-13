package com.crypto.trading_sim.Mappers;

import com.crypto.trading_sim.DTOs.CryptocurrencyDTO;
import com.crypto.trading_sim.Models.Cryptocurrency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CryptocurrencyMapper {


    @Mapping(target = "symbol", source = "symbol")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "rank", source = "rank")
    @Mapping(target = "logoUrl", source = "logoUrl")
    @Mapping(target = "krakenPair", source = "krakenPair")
    CryptocurrencyDTO toDto(Cryptocurrency entity);

    Cryptocurrency toEntity(CryptocurrencyDTO dto);


}
