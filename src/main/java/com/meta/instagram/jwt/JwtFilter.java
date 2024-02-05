package com.meta.instagram.jwt;

import com.meta.instagram.domain.dto.security.CustomAccountDetails;
import com.meta.instagram.domain.entity.Account;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 프론트에서 전달받은 JWT 키를 검증하는 필터
 */
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        System.out.println("authorization now");

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

				//토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

				//토큰에서 email, role 획득
        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);


        // 여기서 정확한 유저 데이터를 가지면 좋겠지만, 인증 절차마다 DB 조회를 한다면 비효율적
        // 따라서 여기선 그냥 username, role만 유효한 객체를 생성해서 details에 넣어주려고 함
        Account account = Account.builder()
                .email(email)
                .username("username")
                .nickname("temp")
                .password("temp")
                .build();
        //UserDetails에 회원 정보 객체 담기
        CustomAccountDetails customUserDetails = new CustomAccountDetails(account);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
