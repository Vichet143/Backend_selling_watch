package com.example.practice.dto;

import com.example.practice.entity.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Set<Role> roles;
}
