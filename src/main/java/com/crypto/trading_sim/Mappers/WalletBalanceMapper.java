package com.crypto.trading_sim.Mappers;

import com.crypto.trading_sim.DTOs.WalletBalanceDTO;
import com.crypto.trading_sim.Models.WalletBalance;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WalletBalanceMapper {

    WalletBalanceDTO toDto(WalletBalance entity);

    WalletBalance toEntity(WalletBalanceDTO dto);
}