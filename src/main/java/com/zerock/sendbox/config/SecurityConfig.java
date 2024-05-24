
package com.zerock.sendbox.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // 비밀번호 암호화
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    public static class UserSecurityConfig {
        @Autowired
        private DataSource dataSource;

        /* 로그인 실패 핸들러 의존성 주입 */
        @Autowired
        private UserCustomFailureHandler customFailureHandler;

        @Bean
        @Order(0)
        public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable);
            // 권한에 따라 허용하는 url 설정
            http.securityMatcher("/user/**", "/", "/logout", "", "/store/**", "/answer/**", "/board/**", "/inquary/**", "/replies/**")
                    .authorizeHttpRequests(
                            (authorizeHttpRequests) ->
                                    authorizeHttpRequests
                                            .requestMatchers("/user/create_account_form",
                                                    "/user/login", "/",
                                                    "/user/terms", "/user/forgot_id", "/user/reset_password_form",
                                                    "/store/**", "/answer/**", "/board/**", "/inquary/**", "/replies/**",
                                                    "/checkPositionLogin", "/checkPositionRegister", "/css/**", "/js/**", "/image/**").permitAll()
                                            .requestMatchers("/user/**").hasRole("USER")
                                            .anyRequest().authenticated()
                    );

            // login 설정
            http
                    .formLogin((formLogin) ->
                            formLogin
                                    .loginPage("/user/login")
                                    .loginProcessingUrl("/user/loginProc")
                                    .usernameParameter("userId")
                                    .passwordParameter("password")
                                    .defaultSuccessUrl("/", true) // 로그인 성공시 타는 경로
                                    .failureHandler(customFailureHandler) // 로그인 실패 핸들러
                                    .permitAll()
                    );

            // logout 설정
            http
                    .logout((logout) ->
                            logout
                                    .logoutSuccessUrl("/")
                                    .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                                    .permitAll()
                    );

            // 접근 거부 핸들러 설정 - 403 에러 대신 유저 로그인 페이지로 리다이렉트
            http.exceptionHandling((exceptionHandling) ->
                    exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                        // 세션을 비워주고 로그인 페이지로 리다이렉트
                        request.getSession().invalidate();
                        response.sendRedirect("/user/login");
                    })
            );

            return http.build();
        }

        // Authentication 로그인 , Authorization 권한
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.jdbcAuthentication()
                    .dataSource(dataSource)
                    .passwordEncoder(passwordEncoder())
                    // 인증처리 >> 아이디/ 비번이 맞으면 꺼내옴
                    .usersByUsernameQuery("select user_id, password, true "
                            + "from user_member "
                            + "where user_id = ? and delete_yn = 'N'")
                    // 권한처리 >> 그에 맞는 role을 가져옴
                    .authoritiesByUsernameQuery("select m.user_id, r.name "
                            + "from user_member m inner join member_role r on m.role_id = r.role_id "
                            + "where m.user_id = ?");
        }
    }

    @Configuration
    public static class OwnerSecurityConfig {
        @Autowired
        private DataSource dataSource;

        /* 로그인 실패 핸들러 의존성 주입 */
        @Autowired
        private OwnerCustomFailureHandler customFailureHandler;

        @Bean
        @Order(1)
        public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable);
            // 권한에 따라 허용하는 url 설정
            http.securityMatcher("/owner/**")
                    .authorizeHttpRequests(
                            (authorizeHttpRequests) ->
                                    authorizeHttpRequests
                                            .requestMatchers("/owner/create_account_form", "/owner/storeInfo", "/owner/login",
                                                    "/owner/terms", "/owner/forgot_id", "/owner/reset_password_form").permitAll()
                                            .requestMatchers("/owner/**").hasRole("OWNER")
                                            .anyRequest().authenticated()
                    );

            // login 설정
            http
                    .formLogin((formLogin) ->
                            formLogin
                                    .loginPage("/owner/login")
                                    .loginProcessingUrl("/owner/loginProc")
                                    .usernameParameter("ownerId")
                                    .passwordParameter("password")
                                    .defaultSuccessUrl("/owner/home", true) // 로그인 성공시 타는 경로
                                    .failureHandler(customFailureHandler) // 로그인 실패 핸들러
                                    .permitAll()
                    );

            // logout 설정
            http
                    .logout((logout) ->
                            logout
                                    .logoutSuccessUrl("/")
                                    .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                                    .permitAll()
                    );

            // 접근 거부 핸들러 설정 - 403 에러 대신 오너 로그인 페이지로 리다이렉트
            http.exceptionHandling((exceptionHandling) ->
                    exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                        // 세션을 비워주고 로그인 페이지로 리다이렉트
                        request.getSession().invalidate();
                        response.sendRedirect("/owner/login");
                    })
            );

            return http.build();
        }

        // Authentication 로그인 , Authorization 권한
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.jdbcAuthentication()
                    .dataSource(dataSource)
                    .passwordEncoder(passwordEncoder())
                    // 인증처리
                    .usersByUsernameQuery("select owner_id, password, true "
                            + "from owner_member "
                            + "where owner_id = ? and delete_yn = 'N' and approval_yn = 'Y'")
                    // 권한처리
                    .authoritiesByUsernameQuery("select m.owner_id, r.name "
                            + "from owner_member m inner join member_role r on m.role_id = r.role_id "
                            + "where m.owner_id = ?");
        }
    }

    @Configuration
    public static class AdminSecurityConfig {
        @Autowired
        private DataSource dataSource;

        /* 로그인 실패 핸들러 의존성 주입 */
        @Autowired
        private AdminCustomFailureHandler customFailureHandler;

        @Bean
        @Order(2)
        public SecurityFilterChain filterChain3(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable);
            // 권한에 따라 허용하는 url 설정
            http.securityMatcher("/admin/**")
                    .authorizeHttpRequests(
                            (authorizeHttpRequests) ->
                                    authorizeHttpRequests
                                            .requestMatchers("/admin/create_account_form", "/admin/login",
                                                    "/admin/terms", "/admin/forgot_id", "/admin/reset_password_form").permitAll()
                                            .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SUPERADMIN")
                                            .anyRequest().authenticated()
                    );

            // login 설정
            http
                    .formLogin((formLogin) ->
                            formLogin
                                    .loginPage("/admin/login")
                                    .loginProcessingUrl("/admin/loginProc")
                                    .usernameParameter("adminId")
                                    .passwordParameter("password")
                                    .defaultSuccessUrl("/admin/home", true)
                                    .failureHandler(customFailureHandler) // 로그인 실패 핸들러
                                    .permitAll()
                    );

            // logout 설정
            http
                    .logout((logout) ->
                            logout
                                    .logoutSuccessUrl("/")
                                    .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                                    .permitAll()
                    );

            // 접근 거부 핸들러 설정 - 403 에러 대신 어드민 로그인 페이지로 리다이렉트
            http.exceptionHandling((exceptionHandling) ->
                    exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                        // 세션을 비워주고 로그인 페이지로 리다이렉트
                        request.getSession().invalidate();
                        response.sendRedirect("/admin/login");
                    })
            );

            return http.build();
        }

        // Authentication 로그인 , Authorization 권한
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.jdbcAuthentication()
                    .dataSource(dataSource)
                    .passwordEncoder(passwordEncoder())
                    // 인증처리
                    .usersByUsernameQuery("select admin_id, password, true "
                            + "from admin_member "
                            + "where admin_id = ? and delete_yn = 'N' and approval = 1")
                    // 권한처리
                    .authoritiesByUsernameQuery("select m.admin_id, r.name "
                            + "from admin_member m inner join member_role r on m.role_id = r.role_id "
                            + "where m.admin_id = ?");
        }
    }

}