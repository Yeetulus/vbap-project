package com.osu.vbap.projectvbap.user;

import com.osu.vbap.projectvbap.exception.ItemNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;

import javax.management.AttributeNotFoundException;
import java.io.IOException;

import static com.osu.vbap.projectvbap.exception.ExceptionMessageUtil.notFoundMessageId;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format(notFoundMessageId, id)));
    }

    @Override
    public User getUserFromRequest(HttpServletRequest request) {
        User user = (User)request.getAttribute("jwtUser");
        if(user == null) throw new AuthorizationServiceException("Could not parse user from JWT token");
        return user;
    }
}