package com.spring.scaffold.user.mapper;

import com.spring.scaffold.user.dto.UserDto;
import com.spring.scaffold.user.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToDto() {
        User user = new User();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");

        UserDto dto = mapper.toDto(user);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Alice");
        assertThat(dto.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void testToEntity() {
        UserDto dto = new UserDto(2L, "Bob", "bob@example.com");

        User user = mapper.toEntity(dto);

        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getName()).isEqualTo("Bob");
        assertThat(user.getEmail()).isEqualTo("bob@example.com");
    }

    @Test
    void testUpdateEntityFromDto() {
        UserDto dto = new UserDto(2L, "Updated", "updated@example.com");

        User user = new User();
        user.setId(1L);
        user.setName("Old");
        user.setEmail("old@example.com");

        mapper.updateEntityFromDto(dto, user);

        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getName()).isEqualTo("Updated");
        assertThat(user.getEmail()).isEqualTo("updated@example.com");
    }
}
