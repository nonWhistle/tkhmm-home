package com.tkhmm.application.security;

import com.tkhmm.application.data.Role;
import com.tkhmm.application.data.entity.User;
import com.tkhmm.application.data.service.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .name("test")
                .username("Test")
                .roles(Set.of(Role.USER))
                .hashedPassword("test")
                .build();
    }

    @DisplayName("Testing user service save method when email is not in use")
    @Test
    void testSaveWhenEmailNotInUse() {
        given(userRepository.findByEmailAddress(user.getEmailAddress())).willReturn(Optional.empty());
        given(userDetailsService.save(user)).willReturn(user);
        User savedUser = userDetailsService.save(user);
        assertThat(savedUser).isNotNull();
    }

    @DisplayName("Testing user service save method when email is already in use")
    @Test
    void testSaveWhenEmailAlreadyInUse() {
        given(userRepository.findByEmailAddress(user.getEmailAddress())).willReturn(Optional.of(user));

        assertThrows(UserDetailsServiceImpl.ResourceNotFoundException.class, () -> {
            userDetailsService.save(user);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("Testing load user by username when user is found")
    @Test
    void testLoadUserByUsernameWhenFound() {
        given(userRepository.findByUsername(user.getUsername())).willReturn(user);

        UserDetails savedUser = userDetailsService.loadUserByUsername(user.getUsername());

        assertThat(savedUser).isNotNull();
    }

    @DisplayName("Testing load user by username when user is NOT found")
    @Test
    void testLoadUserByUsernameWhenUserNotFound() {
        given(userRepository.findByUsername(user.getUsername())).willReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(user.getUsername());
        });
    }
}