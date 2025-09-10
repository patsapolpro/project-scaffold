package com.spring.scaffold.user.service;

import com.spring.scaffold.user.dto.UserDto;
import com.spring.scaffold.user.exception.UserNotFoundException;
import com.spring.scaffold.user.mapper.UserMapper;
import com.spring.scaffold.user.model.User;
import com.spring.scaffold.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    void getAllUsers_returnsMappedList() {
        User user1 = new User(1L, "Alice", "alice@example.com");
        User user2 = new User(2L, "Bob", "bob@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.toDto(user1)).thenReturn(new UserDto(1L, "Alice", "alice@example.com"));
        when(userMapper.toDto(user2)).thenReturn(new UserDto(2L, "Bob", "bob@example.com"));

        List<UserDto> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Alice");
        assertThat(result.get(1).getName()).isEqualTo("Bob");

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_existingUser_returnsDto() {
        User user = new User(1L, "Alice", "alice@example.com");
        UserDto dto = new UserDto(1L, "Alice", "alice@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto result = userService.getUserById(1L);

        assertThat(result.getName()).isEqualTo("Alice");
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void getUserById_notFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void createUser_savesAndReturnsDto() {
        UserDto dto = new UserDto(null, "Charlie", "charlie@example.com");
        User userEntity = new User(null, "Charlie", "charlie@example.com");
        User savedUser = new User(3L, "Charlie", "charlie@example.com");
        UserDto savedDto = new UserDto(3L, "Charlie", "charlie@example.com");

        when(userMapper.toEntity(dto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(savedDto);

        UserDto result = userService.createUser(dto);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Charlie");
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void updateUser_existingUser_updatesAndReturnsDto() {
        UserDto dto = new UserDto(null, "Updated", "updated@example.com");
        User user = new User(1L, "Old", "old@example.com");
        UserDto updatedDto = new UserDto(1L, "Updated", "updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doAnswer(invocation -> {
            UserDto source = invocation.getArgument(0);
            User target = invocation.getArgument(1);
            target.setName(source.getName());
            target.setEmail(source.getEmail());
            return null;
        }).when(userMapper).updateEntityFromDto(dto, user);

        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(updatedDto);

        UserDto result = userService.updateUser(1L, dto);

        assertThat(result.getName()).isEqualTo("Updated");
        assertThat(result.getEmail()).isEqualTo("updated@example.com");
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).updateEntityFromDto(dto, user);
    }

    @Test
    void updateUser_notFound_throwsException() {
        UserDto dto = new UserDto(null, "Updated", "updated@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, dto));
    }

    @Test
    void deleteUser_existingUser_deletes() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_notFound_throwsException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }
}
