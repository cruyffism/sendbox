package com.zerock.sendbox.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {

    // 사용자 정보를 가져오는 메서드를 구현합니다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 여기에 사용자 정보를 가져오는 로직을 구현합니다.
        // 예를 들어, 사용자 이름으로 데이터베이스에서 사용자 정보를 조회하여 UserDetails 객체를 반환합니다.
        // 실제로는 데이터베이스나 다른 저장소에서 사용자 정보를 조회해야 합니다.
        // 이 예제에서는 단순히 더미 사용자를 반환하도록 구현하겠습니다.
        return org.springframework.security.core.userdetails.User.withUsername(username)
                .password("password")
                .roles("USER")
                .build();
    }
}
