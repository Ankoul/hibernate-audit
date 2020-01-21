package com.ctw.audit.authentication.service;

import com.ctw.audit.authentication.model.AuthenticatedUser;
import com.ctw.audit.authentication.model.AuthenticationEvent;
import com.ctw.audit.authentication.model.AuthenticationResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@SuppressWarnings("ALL")
public class RbwBasicAuthenticationService extends BasicAuthentication{

    private final static Logger logger = LoggerFactory.getLogger(RbwBasicAuthenticationService.class);

    @Value("${rainbow.host}")
    private String rainbowApiUrl;

    @Value("${rainbow.appId}")
    private String rainbowAppId;

    @Value("${rainbow.secret}")
    private String rainbowAppSecret;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public AuthenticatedUser authenticate(UserCredentials credentials) {

        if (credentials == null || isBlank(credentials.getUsername()) || isBlank(credentials.getPassword())) {
            throw new BadCredentialsException("you must inform username and password to login");
        }
        HttpEntity<String> requestEntity = buildRequestHeaders(credentials);
        final AuthenticationResponse authResponse = authenticateOnRainbowApi(requestEntity);

        final AuthenticatedUser authenticatedUser = new AuthenticatedUser(authResponse);
        applicationEventPublisher.publishEvent(new AuthenticationEvent(authenticatedUser));
        return authenticatedUser;
    }

    private HttpEntity<String> buildRequestHeaders(UserCredentials credentials) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + this.parseBasicToken(credentials));
        headers.set("x-rainbow-app-auth", "Basic " + this.parseBasicAppToken(credentials));
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>("parameters", headers);
    }

    private String parseBasicToken(UserCredentials credentials) {
        return Base64.encodeBase64String(
                (credentials.getUsername() + ":" + credentials.getPassword()).getBytes(StandardCharsets.UTF_8));
    }

    private String parseBasicAppToken(UserCredentials credentials) {
        final String sha256Hex = DigestUtils.sha256Hex(rainbowAppSecret + credentials.getPassword());
        return Base64.encodeBase64String((rainbowAppId + ":" + sha256Hex).getBytes(StandardCharsets.UTF_8));
    }

    private AuthenticationResponse authenticateOnRainbowApi(HttpEntity<String> entity) {
        RestTemplate restTemplate = new RestTemplate();
        String url = rainbowApiUrl + "/authentication/v1.0/login";
        ResponseEntity<AuthenticationResponse> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, AuthenticationResponse.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                throw new BadCredentialsException("Wrong username or password");
            }
        }
        if(responseEntity == null){
            throw new BadCredentialsException("Wrong username or password");
        }
        return responseEntity.getBody();
    }
}
