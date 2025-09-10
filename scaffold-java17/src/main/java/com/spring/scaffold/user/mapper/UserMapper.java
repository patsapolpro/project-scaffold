package com.spring.scaffold.user.mapper;

import com.spring.scaffold.user.dto.UserDto;
import com.spring.scaffold.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") // Use Spring's @Component
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto dto);

    void updateEntityFromDto(UserDto dto, @MappingTarget User entity);
}
