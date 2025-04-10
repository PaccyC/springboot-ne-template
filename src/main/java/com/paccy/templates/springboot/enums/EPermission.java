package com.paccy.templates.springboot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EPermission {



        ADMIN_READ("admin:read"),
        ADMIN_CREATE("admin:create"),
        CASUAL_READ("casual:read"),
        CASUAL_CREATE("casuak:create");

        @Getter
        private final String permission;

}
