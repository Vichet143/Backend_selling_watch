package com.example.practice.service.impl;

import com.example.practice.config.security.AuthUser;
import com.example.practice.config.security.PasswordConfig;
import com.example.practice.entity.Role;
import com.example.practice.entity.User;
import com.example.practice.exception.ApiException;
import com.example.practice.repository.RoleRepository;
import com.example.practice.repository.UserRepository;
import com.example.practice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.stream.Stream;

import static org.springframework.data.util.Optionals.toStream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordConfig passwordConfig;
    private User user;

    @Override
    public Optional<AuthUser> findUserByUsername(String username){
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        System.out.println("Found user: " + user.getUserName());

        AuthUser authUser = AuthUser.builder()
                .id(user.getId())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .username(user.getUserName())
                .password(user.getPassword())
                .email(user.getEmail())
                .roles(user.getRoles())
                .accountNonExpired(user.isAccountNonExpired())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .enabled(user.isEnabled())
                .build();
        return Optional.ofNullable(authUser);
    }

    private Set<SimpleGrantedAuthority> getAuthorities(Set<Role> roles){
        Set<SimpleGrantedAuthority> authorities1 = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());
        Set<SimpleGrantedAuthority> authorities = roles.stream()
                .flatMap(this::toStream)
                .collect(Collectors.toSet());
        authorities.addAll(authorities1);

        return authorities;
    }

    private Stream<SimpleGrantedAuthority> toStream(Role role){
        return role.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()));
    }

    @Override
    public User createuser(User user) {
        user.setPassword(passwordConfig.passwordEncoder().encode(user.getPassword()));

        try{
            Set<Role> fullRoles = user.getRoles().stream()
                    .map(role -> roleRepository.findById(role.getId())
                            .orElseThrow(() -> new RuntimeException("Role not found: " + role.getId())))
                    .collect(Collectors.toSet());
            user.setUserName(user.getFirstName() + " "+ user.getLastName());
            user.setRoles(fullRoles);
            return  userRepository.save(user);
        }catch (Exception e){
            throw new ApiException(HttpStatus.BAD_REQUEST,"false",e.getMessage());
        }

    }

    @Override
    public Optional<AuthUser> findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        AuthUser authUser = AuthUser.builder()
                .id(user.getId())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .username(user.getUserName())
                .password(user.getPassword())
                .email(user.getEmail())
                .roles(user.getRoles())
                .authorities(getAuthorities(user.getRoles()))
                .accountNonExpired(user.isAccountNonExpired())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .enabled(user.isEnabled())
                .build();
        return Optional.ofNullable(authUser);
    }
}
