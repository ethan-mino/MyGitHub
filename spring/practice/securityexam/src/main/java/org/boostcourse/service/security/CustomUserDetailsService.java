package org.boostcourse.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// UserDetailsService는 스프링 시큐리티가 제공하는 인터페이스이다. 해당 인터페이스를 구현하고 있는 객체가 Bean으로 등록되어 있어야 한다.
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserDbService userDbService;

    // 사용자가 로그인할 때 아이디를 입력하면 해당 아이디를 loadUserByUsername() 메서드의 인자로 전달한다. 해당 아이디에 해당하는 정보가 없으면 UsernameNotFountException이 발생한다.
    // 정보가 있을 경우엔 UserDetails 인터페이스를 구현한 객체를 리턴한다.
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        // loginIdp 해당 하는 정보를 데이터베이스에서 읽어 CustonUser 객체에 저장한다.
        // 해당 정보를 CustomUserDetails 객체에 저장한다.
        UserEntity customUser = userDbService.getUser(loginId);

        if(customUser == null)
            throw new UsernameNotFoundException("사용자가 입력한 아이디에 해당하는 사용자를 찾을 수 없습니다.");

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUsername(customUser.getLoginUserId());
        customUserDetails.setPassword(customUser.getPassword());

        List<UserRoleEntity> customRoles = userDbService.getUserRoles(loginId);
        // 로그인 한 사용자의 권한 정보를 GrantedAuthority를 구현하고 있는 SimpleGrantedAuthority 객체에 담아 리스트에 추가한다.
        // MemberRole 이름은 "ROLE_"로 시작되어야 한다.
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(customRoles != null){
            for (UserRoleEntity customRole : customRoles){
                authorities.add(new SimpleGrantedAuthority(customRole.getRoleName()));
            }
        }

        // CustomUserDetailes 객체에 권한 목록(authorities)를 설정한다.
        customUserDetails.setAuthorities(authorities);
        customUserDetails.setEnabled(true);
        customUserDetails.setAccountNonExpired(true);
        customUserDetails.setAccountNonLocked(true);
        customUserDetails.setCredentialsNonExpired(true);

        return customUserDetails;
    }
}
