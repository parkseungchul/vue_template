package com.example.backend.annotation;

import com.example.backend.dto.TokenStatus;
import com.example.backend.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Aspect
@Component
public class AuthenticationAspect {
    final JwtService jwtService;

    public AuthenticationAspect(JwtService jwtService){
        this.jwtService = jwtService;
    }

    @Around("@annotation(authenticatedEndpoint)")
    public Object authenticate(ProceedingJoinPoint joinPoint, AuthenticatedEndpoint authenticatedEndpoint) throws Throwable {
        HttpServletRequest request = getCurrentHttpRequest(joinPoint);
        HttpServletResponse response = getCurrentHttpResponse(joinPoint);

        Cookie[] cookies = request.getCookies();
        Integer tokenId = null;
        String token = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("tokenId".equals(cookie.getName())) {
                    try {
                        tokenId = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        handleUnauthorized(authenticatedEndpoint, response);
                    }
                } else if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
        TokenStatus tokenStatus = null;
        if (tokenId == null || token == null) {
            handleUnauthorized(authenticatedEndpoint, response);
        }else{
            tokenStatus = jwtService.getClaims(tokenId, token);
            if (tokenStatus == null) {
                handleUnauthorized(authenticatedEndpoint, response);
            }else{
                jwtService.setCookies(tokenStatus, response);
                request.setAttribute("tokenStatus", tokenStatus);
            }
        }
        Object result = joinPoint.proceed();
        return result;
    }

    private HttpServletRequest getCurrentHttpRequest(ProceedingJoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof HttpServletRequest) {
                return (HttpServletRequest) arg;
            }
        }
        throw new IllegalStateException("Method does not have HttpServletRequest argument");
    }

    private HttpServletResponse getCurrentHttpResponse(ProceedingJoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof HttpServletResponse) {
                return (HttpServletResponse) arg;
            }
        }
        throw new IllegalStateException("Method does not have HttpServletResponse argument");
    }

    // process optional
    private void handleUnauthorized(AuthenticatedEndpoint authenticatedEndpoint, HttpServletResponse response) throws IOException {
        if (authenticatedEndpoint.throwOnUnauthorized()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } else {

        }
    }
}
