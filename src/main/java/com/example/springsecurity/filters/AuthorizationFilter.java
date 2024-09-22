package com.example.springsecurity.filters;

import com.example.springsecurity.constants.SecurityConstants;
import com.example.springsecurity.impl.UserPrincipal;
import com.example.springsecurity.entities.UserEntity;
import com.example.springsecurity.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class AuthorizationFilter extends BasicAuthenticationFilter {

    @Value("${tokenSecret}")
    private String tokenSecret;

    private final UserRepository userRepository;


    public AuthorizationFilter(AuthenticationManager authManager, UserRepository userRepository) {
        super(authManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        if (token != null) {

            token = token.replace(SecurityConstants.TOKEN_PREFIX, "");
            String username = Jwts.parser()
                    .setSigningKey(tokenSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            if (username != null) {
                UserEntity userEntity = userRepository.findByUsername(username);
                UserPrincipal userPrincipal = new UserPrincipal(userEntity);
                return new UsernamePasswordAuthenticationToken(username, null, userPrincipal.getAuthorities());
            }
            return null;
        }
        return null;
    }


}
