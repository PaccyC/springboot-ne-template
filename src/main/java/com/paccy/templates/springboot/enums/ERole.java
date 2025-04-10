package com.paccy.templates.springboot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public enum ERole {
    ADMIN(
            Set.of(
                    EPermission.ADMIN_CREATE,
                    EPermission.ADMIN_READ
            )
    ),
    CASUAL(
            Set.of(
                    EPermission.CASUAL_READ,
                    EPermission.CASUAL_CREATE
            )
    );

    @Getter
    private final Set<EPermission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
            authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
          return  authorities;

    }
}
