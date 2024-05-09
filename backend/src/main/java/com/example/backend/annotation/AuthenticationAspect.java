package com.example.backend.annotation;

import com.example.backend.dto.TokenStatus;
import com.example.backend.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

/**
 * 이 클래스는 @Authentication 를 사용하기 위한 것이다.
 * This class is for using @Authentication
 *
 * 이 어노테이션의 옵션으로 throwOnUnauthorized 있으며 이는 interface 에 정의 되어 있다
 * The annotation option is the 'throwOnUnauthorized' method.
 * It is defined in the Authentication interface.
 */
@Slf4j
@Aspect
@Component
public class AuthenticationAspect {
    final JwtService jwtService;

    public AuthenticationAspect(JwtService jwtService){
        this.jwtService = jwtService;
    }

    /**
     * Request 에서 Cookies 를 가져오고, Token 은 Cookies 만들어진다. Token 을 세션에 넣는다.
     * It retrieves the Cookies from Request, it derives Token which is made them, put it into the session
     * @param joinPoint
     * @param authenticatedEndpoint
     * @return
     * @throws Throwable
     */
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
        // if it doesn't have Token information.
        TokenStatus tokenStatus = null;
        if (tokenId == null || token == null) {
            handleUnauthorized(authenticatedEndpoint, response);
            log.debug("token or tokenId is null");
        }else{ // Get the latest token through "getClaim" method.
            tokenStatus = jwtService.getClaims(tokenId, token);
            // it the refresh token is also expired, it should return 'null'
            if (tokenStatus == null) {
                log.debug("tokenStatus is null");
                handleUnauthorized(authenticatedEndpoint, response);
            // If it is correct, it should return 'TokenStatus'
            }else{
                log.debug("tokenStatus is ok");
                jwtService.setCookies(tokenStatus, response);
                request.setAttribute("tokenStatus", tokenStatus);
            }
        }
        // The method of the controller is executed.
        Object result = joinPoint.proceed();
        return result;
    }

    /**
     * Request 있는지 확인
     * Verify whether there is a Request or not
     * @param joinPoint
     * @return
     */
    private HttpServletRequest getCurrentHttpRequest(ProceedingJoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof HttpServletRequest) {
                return (HttpServletRequest) arg;
            }
        }
        throw new IllegalStateException("Method does not have HttpServletRequest argument");
    }

    /**
     * Response 있는지 확인
     * Verify whether there is a Response or not
     * @param joinPoint
     * @return
     */
    private HttpServletResponse getCurrentHttpResponse(ProceedingJoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof HttpServletResponse) {
                return (HttpServletResponse) arg;
            }
        }
        throw new IllegalStateException("Method does not have HttpServletResponse argument");
    }


    /**
     * 이것은 권한을 사용할지 않할지 식별하는 곳에 사용될 수 있다.
     * It can be use to identify whether to use an authority or not
     * @param authenticatedEndpoint
     * @param response
     * @throws IOException
     */
    private void handleUnauthorized(AuthenticatedEndpoint authenticatedEndpoint, HttpServletResponse response) throws IOException {
        if (authenticatedEndpoint.throwOnUnauthorized()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } else {

        }
    }
}
