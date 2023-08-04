package com.sh.global.util;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.repository.UserRepository;
import com.sh.domain.user.util.UserStatus;
import com.sh.global.exception.customexcpetion.user.UserNotFoundException;
import com.sh.global.exception.customexcpetion.user.UserWithdrawalException;
import com.sh.global.exception.errorcode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        if(user.getStatus() == UserStatus.WITHDRAWN) {
            throw new UserWithdrawalException(UserErrorCode.WITHDRAWN_USER);
        }

        return createUserDetails(user);
    }

    private UserDetails createUserDetails(User user) {
        return CustomUserDetails.from(user);
    };
}
