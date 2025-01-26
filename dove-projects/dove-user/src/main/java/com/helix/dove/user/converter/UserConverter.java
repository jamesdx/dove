package com.helix.dove.user.converter;

import com.helix.dove.user.dto.UserRegistrationDTO;
import com.helix.dove.user.dto.UserResponseDTO;
import com.helix.dove.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * User Converter
 */
@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    /**
     * Convert UserRegistrationDTO to User
     *
     * @param dto UserRegistrationDTO
     * @return User
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "1")
    @Mapping(target = "failedAttempts", constant = "0")
    @Mapping(target = "lastLoginTime", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    @Mapping(target = "lockedUntil", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    User toUser(UserRegistrationDTO dto);

    /**
     * Convert User to UserResponseDTO
     *
     * @param user User
     * @return UserResponseDTO
     */
    UserResponseDTO toUserResponseDTO(User user);
} 