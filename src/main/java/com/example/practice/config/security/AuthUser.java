package com.example.practice.config.security;

import com.example.practice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthUser implements UserDetails {

    private Long id;

    private String firstname;

    private String lastname;

    private String username;

    private String password;

    private String email;

    private Set<Role> roles;

    private Set<? extends GrantedAuthority> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (authorities == null) {
            return Set.of();
        }

        return authorities;
    }

    @Override
    public String getPassword() {

        return password;
    }

    @Override
    public String getUsername() {

        return username;
    }

    @Override
    public boolean isAccountNonExpired() {

        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {

        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {

        return enabled;
    }
}