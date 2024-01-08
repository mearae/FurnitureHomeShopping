package com.example.funitureOnlineShop.core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.funitureOnlineShop.user.StringArrayConverter;
import com.example.funitureOnlineShop.user.User;
import org.springframework.security.core.Authentication;

import java.util.Date;

public class JwtTokenProvider {

    // ** JWT 토큰의 만료 시간을 1시간으로 설정.
    private static final Long EXP = 1000L * 60 * 60;

    // ** JWT 갱신 토큰의 만료 시간을 60일로 설정.
    private static final Long REFRESH_EXP = 1000L * 60 * 60 * 24 * 60;

    // ** 인증 헤더에 사용될 토큰의 접두어 ("Bearer ")
    public static final String TOKEN_PREFIX = "Bearer ";
    
    // ** 인증 헤더의 이름을 "Authorization"으로 설정.
    public static final String HEADER = "Authorization";
    
    // ** 토큰의 서명을 생성하고 검증할 때 사용하는 비밀 키
    private static final String SECRET = "SECRET_KEY";

    // ** User 객체의 정보를 사용해 JWT 토큰을 생성하고 반환.
    public static String create(User user) {

        // ** StringArrayConverter 객체 생성
        StringArrayConverter stringArrayConverter = new StringArrayConverter();

        // ** User의 권한 정보를 String 로 변경
        String roles = stringArrayConverter.convertToDatabaseColumn(
                user.getRoles()
        );

        String jwt = JWT.create()
                .withSubject(user.getEmail()) // ** 토큰의 대상정보 셋팅 
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP)) // ** 시간 설정
                .withClaim("id", user.getId()) // ** id설정
                .withClaim("roles", roles) // ** 권한정보 설정
                .sign(Algorithm.HMAC512(SECRET)); // ** jwt 생성 알고리즘 설정

        return TOKEN_PREFIX + jwt;
    }

    public static String createRefresh(User user){
        String jwt = JWT.create()
                .withSubject(user.getEmail())// ** 토큰의 대상정보 셋팅
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .sign(Algorithm.HMAC512(SECRET));// ** JWT 생성 알고리즘 설정
        return jwt;
    }

    // **  JWT 토큰 문자열을 검증하고, 유효하다면 디코딩된 DecodedJWT 객체를 반환.
    public static DecodedJWT verify(String jwt) throws SignatureVerificationException, TokenExpiredException {

        // ** 토큰 검증을 시작.
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET))
                .build()
                .verify(jwt);

        return decodedJWT;
    }

    public static void invalidateToken(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // 현재 인증된 사용자의 토큰을 무효화하는 로직을 구현합니다.
            // 예를 들어, 토큰을 블랙리스트에 추가하거나, 토큰의 만료일을 조정하여 무효화할 수 있습니다.
            // 구체적인 구현 방법은 프로젝트의 요구사항과 토큰 관리 방식에 따라 다를 수 있습니다.
            // 예시로는 블랙리스트에 추가하는 방법을 보여드리겠습니다.

            String token = (String) authentication.getCredentials();
            Blacklist.addToken(token);
        }
    }
}




















