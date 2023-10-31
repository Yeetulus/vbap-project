package com.osu.vbap.projectvbap.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.osu.vbap.projectvbap.user.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    LIBRARIAN_READ,
                    LIBRARIAN_UPDATE,
                    LIBRARIAN_DELETE,
                    LIBRARIAN_CREATE,
                    MEMBER_READ,
                    MEMBER_UPDATE,
                    MEMBER_DELETE,
                    MEMBER_CREATE
            )
    ),
    LIBRARIAN(
            Set.of(
                    LIBRARIAN_READ,
                    LIBRARIAN_UPDATE,
                    LIBRARIAN_DELETE,
                    LIBRARIAN_CREATE
            )
    ),
    MEMBER(
            Set.of(
                    MEMBER_READ,
                    MEMBER_UPDATE,
                    MEMBER_DELETE,
                    MEMBER_CREATE
            )
    )

    ;

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}