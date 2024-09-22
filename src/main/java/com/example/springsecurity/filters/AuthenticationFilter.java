package com.example.springsecurity.filters;

import com.example.springsecurity.models.responses.LoginResponseModel;
import com.example.springsecurity.constants.SecurityConstants;
import com.example.springsecurity.models.requests.LoginRequest;
import com.example.springsecurity.impl.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequest creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.username(), creds.password(), new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        UserPrincipal userPrincipal = (UserPrincipal) authResult.getPrincipal();
        String userName = userPrincipal.getUsername();
        UUID userId = userPrincipal.getUserId();
        String firstName = userPrincipal.getFirstName();
        String lastName = userPrincipal.getLastName();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("firstName", firstName);
        claims.put("lastName", lastName);

        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        String accessToken = Jwts.builder()
                .setSubject(userName)
                .claims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(userName)
                .claims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.REFRESH_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();

        LoginResponseModel loginResponse = new LoginResponseModel();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setExpiresIn((int) (SecurityConstants.EXPIRATION_TIME));
        loginResponse.setRefreshToken(refreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), loginResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", 401);
        errorResponse.put("message", "Unauthorized");
        errorResponse.put("errors", Collections.singletonList("Sorry! The username and password combination do not match."));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }



}
