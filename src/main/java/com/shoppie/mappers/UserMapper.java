package com.shoppie.mappers;

import com.shoppie.entities.User;
import com.shoppie.payloads.user.UserRegisterRequest;
import com.shoppie.payloads.user.UserResponse;
import com.shoppie.payloads.user.UserUpdateRequest;
import org.mapstruct.*;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserMapper {
    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "bio", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "encodedPassword", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserRegisterRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "encodedPassword", ignore = true)
    @Mapping(target = "role", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UserUpdateRequest request, @MappingTarget User user);
}
