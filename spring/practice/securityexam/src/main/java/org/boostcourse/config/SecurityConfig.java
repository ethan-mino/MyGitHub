package org.boostcourse.config;

import org.boostcourse.service.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity  // 스프링 시큐리티를 구성하는 기본적인 빈(Bean)들을 자동으로 구성
// 스프링 시큐리티를 이용해서 로그인/로그아웃/인증/인가 등을 처리하기 위한 설정 파일
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {  // 인증/인가가 필요 없는 경로를 설정할 필요가 있을 때 오버라이딩
        web.ignoring().antMatchers("/webjars/**");  // 해당 경로의 파일들은 Spring Security가 무시하도록 설정. (자바 개발 도구 중 Maven처럼 프로젝트를 빌드할 때 사용하는 ant라는 도구가 있는데, Maven 이전에 널리 사용되았던 도구였다. 이 ant에서 사용하는 표기법 중에 "/경로/**"와 같은 형식에서 "**"은 특정 경로 이하의 모든 것을 의미한다.)
                                                               // 즉, "/webjars/"로 시작하는 모든 경로를 무시하라는 의미이다.
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);  // AuthenticationFilter는 아이디/암호를 입력해서 로그인 할 떄 처리해주는 필터이고, 아이디에 해당하는 정보를 데이터베이스에서 읽어들일 때 UserDetailsService를 구현한 객체를 이용한다.
                                                            // UserDetailsService는 인터페이스이고, 해당 인터페이스를 구현하고 있는 빈을 사용한다.
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {  // 인증/인가에 대한 설정
        http.csrf().disable()   // csrf란 보안 설정 중에서 POST 방식으로 값을 전송할 때, 토큰을 사용하는 보안 설정이다. CSRF가 기본으로 설정되는데, CSRF를 사용하며 보안성은 높아지지만,개발 초기에는 불편함을 가진다는 단점이 있어 disable함. (disable() 메서드는 HttpSecurity를 다시 반환함.)
            .authorizeRequests()
            .antMatchers("/", "/main", "/members/loginerror", "/members/joinform", "/members/join", "/members/welcome").permitAll()  // "/", "main", "/members/welcome", "/members/joinform", "/loginerror"에 대해서는 누구나 접근(permitAll) 가능하도록 설정 (그렇다면 web.ignore()에 왜 설정하지 않았나?)
            .antMatchers("/securepage", "/members/**").hasRole("USER")  // "/members/**", "/secourpage"에 대해서는 로그인도 되어 있어야하고, "USER"라는 권한도 가지고 있어야지만 접근할 수 있도록 설정
                                                                                   // hasRole()은 현재 로그인된 사용자가 지정된 role을 가지고 있으면 true를 반환한다.제공된 role이 "ROLE_"로 시작하지 않으면 기본적으로 "ROLE_"을 추가한다. 이것으 DefaultWebSecurityExpresionHandler에서 defaultRolePrifix를 수정하여 커스터마이징 할 수 있다. (https://offbyone.tistory.com/91 참조)
            .anyRequest().authenticated() // 그 외의 경로에 대해서는 모두 인증을 거쳐야지만 접근할 수 있도록 설정
            // login Form에 대해 설정
            .and()
                .formLogin()
                .loginPage("/members/loginform")    // "/members/loginform" 경로로 요청이 왔을 때 로그인 폼을 보여주는 컨트롤러 메서드를 작성해야한다.
                .usernameParameter("userId")    // 아이디를 입력받는 input 태그의 name이 "userId"이어야 함.
                .passwordParameter("password")  // 비밀번호를 입력받는 input 태그의 name이 "password"이어야 함.
                .loginProcessingUrl("/authenticate")    // 처리 경로 (form 태그의 action과 같다. 개발자가 직접 구현하는 것은 아니고, 이렇게 설정해주면 id와 암호를 입력받아서 로그인을 처리하는 Spring Security Filter가 해당 경로를 검사하다가 id와 암호가 전달되면 로그인 과정을 처리)
                .failureForwardUrl("/members/loginerror?login_error=1")    // 로그인에 실패한 경우 forward할 url 지정
                .defaultSuccessUrl("/", true)   // 로그인에 성공한 경우 리다이렉트할 url 지정
                .permitAll()    // login form은 누구나 접근 가능
            // logout에 대한 설정
            .and()  // logout에 대한 요청이 들어오면 세션에서 로그인 정보를 삭제한 후에 "/"로 리다이렉트
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");

    }

    @Bean
    public PasswordEncoder encoder(){   // 아이디/암호를 입력해서 로그인 처리를 하려면 반드시 인코딩(encoding)되어 있어야한다.
        return new BCryptPasswordEncoder(); // PasswordEncoder 객체는 암호를 인코딩하거나 인코딩된 암호와 사용자가 입력한 암호가 일치하는지 확인하는데 사용된다.
    }
}
