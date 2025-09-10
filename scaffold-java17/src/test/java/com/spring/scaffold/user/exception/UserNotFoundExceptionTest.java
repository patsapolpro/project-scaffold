package com.spring.scaffold.user.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserNotFoundExceptionTest {

    @Test
    void constructor_setsMessage() {
        String message = "User with id 42 not found";

        UserNotFoundException ex = new UserNotFoundException(message);

        assertThat(ex).isInstanceOf(RuntimeException.class);
        assertThat(ex.getMessage()).isEqualTo(message);
    }
}
