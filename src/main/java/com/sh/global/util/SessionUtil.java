package com.sh.global.util;

import com.sh.global.exception.errorcode.UserErrorCode;
import com.sh.global.exception.customexcpetion.user.UserNonLoginException;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionUtil {

    private final String SESSION_ID = "userId";
    private final int MAX_INACTIVE_INTERVAL = 60 * 60 * 30;

    private final HttpSession session;

    public void setAttribute(Long id) {
        session.setAttribute(SESSION_ID, id);
        session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
    }

    public Long getAttribute() {
        if (session.getAttribute(SESSION_ID) == null) {
            throw new UserNonLoginException(UserErrorCode.NON_LOGIN);
        }
        return Long.parseLong(String.valueOf(session.getAttribute(SESSION_ID)));
    }

    public void invalidate() {
        session.invalidate();
    }
}
