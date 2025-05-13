package com.crypto.trading_sim.Mappers;

import com.crypto.trading_sim.Models.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;
import com.crypto.trading_sim.DTOs.UserDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {


    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "balanceUsd", source = "balanceUsd")
    UserDTO toDto(User user);

    User toEntity(UserDTO dto);
}
