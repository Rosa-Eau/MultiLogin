package org.example.multiplelogin.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.multiplelogin.domain.owner.repository.OwnerRepository;
import org.example.multiplelogin.domain.user.dto.LoginRequestRecord;
import org.example.multiplelogin.domain.user.repository.UserRepository;
import org.example.multiplelogin.handler.exception.BusinessException;
import org.example.multiplelogin.handler.exception.ErrorCode;
import org.example.multiplelogin.security.owner.OwnerDetailsImpl;
import org.example.multiplelogin.security.user.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            LoginRequestRecord requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestRecord.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.email(),
                            requestDto.password(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");

        String email = "";

        if (authResult.getPrincipal() instanceof OwnerDetailsImpl) {
            email = ((OwnerDetailsImpl) authResult.getPrincipal()).getUsername();
        } else if (authResult.getPrincipal() instanceof UserDetailsImpl) {
            email = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        }

        String token = jwtUtil.createToken(email);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");

        // 실패한 이유에 따라 적절한 에러 코드를 설정합니다.
        String errorCode = ErrorCode.NOT_MATCH_EMAIL_PASSWORD.getCode();
        String errorMessage = ErrorCode.NOT_MATCH_EMAIL_PASSWORD.getMessage();

        // 에러 코드와 메시지를 JSON 형태로 응답합니다.
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 예시로 400 상태코드를 설정합니다.
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"ErrorCode\":\"" + errorCode + "\", \"ErrorMessage\":\"" + errorMessage + "\"}");
    }
}