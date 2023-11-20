package com.osu.vbap.projectvbap.user;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    User getUser(Long id);
    User getUserFromRequest(HttpServletRequest request);
}
