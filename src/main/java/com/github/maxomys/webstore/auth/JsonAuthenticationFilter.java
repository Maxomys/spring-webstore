package com.github.maxomys.webstore.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token;

        try {
            String requestData = request.getReader().lines().collect(Collectors.joining());
            LoginCredentials authRequest = objectMapper.readValue(requestData, LoginCredentials.class);

            token = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword());
            setDetails(request, token);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        return this.getAuthenticationManager().authenticate(token);
    }

}
