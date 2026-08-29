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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordConfig passwordConfig;

    @Override
    public Optional<AuthUser> findUserByUsername(String username) {

        User user = userRepository.findByUserName(username)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found with username: " + username
                        )
                );

        System.out.println(
                "Found user by username: " + user.getUserName()
        );

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

        return Optional.of(authUser);
    }

    @Override
    public Optional<AuthUser> findUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email: " + email
                        )
                );

        System.out.println(
                "Found user by email: " + user.getEmail()
        );

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

        return Optional.of(authUser);
    }

    @Override
    public User findUserEntityByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email: " + email
                        )
                );
    }

    private Set<SimpleGrantedAuthority> getAuthorities(
            Set<Role> roles
    ) {

        if (roles == null || roles.isEmpty()) {
            return Set.of();
        }

        Set<SimpleGrantedAuthority> authorities =
                roles.stream()
                        .map(role ->
                                new SimpleGrantedAuthority(
                                        "ROLE_" + role.getName()
                                )
                        )
                        .collect(Collectors.toSet());

        authorities.addAll(
                roles.stream()
                        .flatMap(this::toStream)
                        .collect(Collectors.toSet())
        );

        return authorities;
    }

    private Stream<SimpleGrantedAuthority> toStream(
            Role role
    ) {

        if (role.getPermissions() == null) {
            return Stream.empty();
        }

        return role.getPermissions()
                .stream()
                .map(permission ->
                        new SimpleGrantedAuthority(
                                permission.getName()
                        )
                );
    }

    @Override
    public User createuser(User user) {

        // Encrypt password before saving
        user.setPassword(
                passwordConfig
                        .passwordEncoder()
                        .encode(user.getPassword())
        );

        try {

            Set<Role> fullRoles =
                    user.getRoles()
                            .stream()
                            .map(role ->
                                    roleRepository
                                            .findById(role.getId())
                                            .orElseThrow(() ->
                                                    new RuntimeException(
                                                            "Role not found: "
                                                                    + role.getId()
                                                    )
                                            )
                            )
                            .collect(Collectors.toSet());

            user.setUserName(
                    user.getFirstName()
                            + " "
                            + user.getLastName()
            );

            user.setRoles(fullRoles);

            return userRepository.save(user);

        } catch (Exception e) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "false",
                    e.getMessage()
            );
        }
    }

    @Override
    public User findById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "false",
                                "user not found"
                        )
                );
    }
}