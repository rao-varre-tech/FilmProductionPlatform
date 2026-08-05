package com.eee.filmplatform.services.identity.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.eee.filmplatform.services.identity.entity.Permission;
import com.eee.filmplatform.services.identity.entity.Role;
import com.eee.filmplatform.services.identity.entity.RolePermission;
import com.eee.filmplatform.services.identity.entity.User;
import com.eee.filmplatform.services.identity.entity.UserRole;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();

        for (UserRole userRole : user.getUserRoles()) {

            Role role = userRole.getRole();

            // Role authority
            authorities.add(
                    new SimpleGrantedAuthority(
                            "ROLE_" + role.getName().name()));

            // Permission authorities
            for (RolePermission rolePermission : role.getRolePermissions()) {

                Permission permission = rolePermission.getPermission();

                authorities.add(
                        new SimpleGrantedAuthority(
                                permission.getName().name()));
            }
        }

        authorities.forEach(authority -> System.out.println("Authority: " + authority.getAuthority()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // We'll support email later.
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}