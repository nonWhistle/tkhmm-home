package com.tkhmm.application.security;

import com.tkhmm.application.data.Role;
import com.tkhmm.application.data.entity.User;
import com.tkhmm.application.data.service.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getHashedPassword(),
                    getAuthorities(user));
        }
    }

    public User save(User user) {
        Optional<User> savedEmployee = userRepository.findByEmailAddress(user.getEmailAddress());
        if(savedEmployee.isPresent()){
            throw new ResourceNotFoundException("User already exists with the passed email");
        }
        return userRepository.save(user);
    }

    

    private static List<GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

    }


    public class ResourceNotFoundException extends RuntimeException {

        @Getter
        private String message;
        public ResourceNotFoundException(String message) {
            super();
            this.message = message;
        }
    }
}
