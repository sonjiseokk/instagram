package com.meta.instagram.config;

import com.meta.instagram.jwt.JwtFilter;
import com.meta.instagram.jwt.JwtUtil;
import com.meta.instagram.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->
                web.ignoring()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                        .anyRequest();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager 를 반환하는 메소드
     * 현재 UsernamePasswordAuthenticationFilter 를 대체하는 LoginFilter가 AuthenticationManager 가 없는 상태
     * 따라서 주입해주기 위해 생성
     * @param configuration 스프링 시큐리티 매니저, 프로바이저 등을 관리하는 객체
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CORS 세팅
        http.cors(cors -> cors
                .configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));
        // csrf disable -> JWT를 사용할 건데 JWT는 비동기라 csrf 공격 무의미
        http.csrf((auth) -> auth.disable());

        // 폼 로그인 방식 disable -> JWT를 사용할 것이기 때문
        http.formLogin((auth) -> auth.disable());

        // 베이직 로그인 disable -> 마찬가지로 JWT 사용 + 이건 보안상 이슈
        http.httpBasic((auth) -> auth.disable());

        // 로그인, 메인, 회원가입 제외하곤 전부 인증

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/register").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated());

        // JWT는 무상태 프로토콜이기때문에 꼭 선언해줘야함
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);
        // 필터를 대체하는 메소드
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
