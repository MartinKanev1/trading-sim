package com.crypto.trading_sim.Mappers;

import com.crypto.trading_sim.DTOs.PriceSnapshotDTO;
import com.crypto.trading_sim.Models.PriceSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PriceSnapshotMapper {

    PriceSnapshotDTO toDto(PriceSnapshot entity);


    PriceSnapshot toEntity(PriceSnapshotDTO dto);
}
