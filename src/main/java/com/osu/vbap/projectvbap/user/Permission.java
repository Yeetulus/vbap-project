package com.osu.vbap.projectvbap.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    LIBRARIAN_READ("library:read"),
    LIBRARIAN_UPDATE("library:update"),
    LIBRARIAN_CREATE("library:create"),
    LIBRARIAN_DELETE("library:delete"),
    MEMBER_READ("member:read"),
    MEMBER_UPDATE("member:update"),
    MEMBER_CREATE("member:create"),
    MEMBER_DELETE("member:delete")

    ;

    private final String permission;
}