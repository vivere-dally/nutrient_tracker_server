package com.ubb.ppd.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubb.ppd.config.WebSecurityConfigConstants;
import com.ubb.ppd.domain.model.dto.UserDTO;
import com.ubb.ppd.webservice.exceptionHandlers.ErrorResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(@Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            UserDTO applicationUser = new ObjectMapper().readValue(req.getInputStream(), UserDTO.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            applicationUser.getUsername(),
                            applicationUser.getPassword(),
                            new ArrayList<>())
            );
        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            setErrorResponse(HttpStatus.FORBIDDEN, res, ex);
            res.setStatus(403);
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) {

        Date accessExp = new Date(System.currentTimeMillis() + WebSecurityConfigConstants.ACCESS_TOKEN_EXPIRATION_TIME);
        Key key = Keys.hmacShaKeyFor(WebSecurityConfigConstants.KEY.getBytes());
        String accessToken = Jwts
                .builder()
                .signWith(SignatureAlgorithm.HS512, key)
                .setExpiration(accessExp)
                .compact();
        res.setHeader("accessToken", accessToken);
        res.setHeader("Access-Control-Expose-Headers", "accessToken");
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        ErrorResponse apiError = new ErrorResponse(status.value(), ex.getMessage(), LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        try {
            response.getWriter().write(apiError.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
