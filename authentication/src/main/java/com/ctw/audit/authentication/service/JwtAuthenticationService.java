package com.ctw.audit.authentication.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class JwtAuthenticationService extends BasicAuthentication{

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 10; // 10 days
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret}")
    private String secret;

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    public JwtAuthenticationService(@Qualifier("userDetailsService") UserDetailsService userDetailsService,
                                    PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Authentication authenticate(UserCredentials credentials)  {
        if (credentials == null || isBlank(credentials.getUsername()) || isBlank(credentials.getPassword())) {
            throw new BadCredentialsException("you must inform username and password to login");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getUsername());
        if(userDetails == null || !passwordEncoder.matches(credentials.getPassword(), userDetails.getPassword())){
            throw new BadCredentialsException("username and password doesn't match");
        }

        String token = this.generateToken(userDetails.getUsername());
        PreAuthenticatedAuthenticationToken auth =
                new PreAuthenticatedAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        auth.eraseCredentials();
        return auth;
    }

    private String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, Base64.encodeBase64(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    Authentication authenticate(HttpServletRequest request) {

        String token = request.getHeader(AUTHORIZATION_HEADER);
        if(token == null || !token.startsWith(BEARER_PREFIX)){
            return null;
        }

        String username = this.extractTokenSubject(token);
        if(username == null){
            return null;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        PreAuthenticatedAuthenticationToken auth =
                new PreAuthenticatedAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        auth.eraseCredentials();
        return auth;
    }

    private String extractTokenSubject(String token) {
        if (token == null) {
            return null;
        }
        return Jwts.parser()
                .setSigningKey(Base64.encodeBase64(secret.getBytes(StandardCharsets.UTF_8)))
                .parseClaimsJws(token.replace(BEARER_PREFIX, ""))
                .getBody()
                .getSubject();
    }
}
