package com.crypto.trading_sim.Mappers;

import com.crypto.trading_sim.DTOs.TransactionDTO;
import com.crypto.trading_sim.Models.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {


    @Mapping(target = "profitLoss", ignore = true)
    TransactionDTO toDto(Transaction entity);


    Transaction toEntity(TransactionDTO dto);
}
