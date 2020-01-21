package com.ctw.audit.authentication.service;

import com.ctw.audit.authentication.model.AuthenticatedUser;
import com.ctw.audit.authentication.model.ResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@SuppressWarnings({"SpringJavaAutowiringInspection"})
public class RbwBearerAuthenticationService {

    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${rainbow.host}")
    private String rainbowApiUrl;

    @Value("${rainbow.appId}")
    private String applicationId;

    Authentication authenticate(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !mayBeBearerToken(token)) {
            return AuthenticatedUser.instanceNotAuthenticatedUser();
        }
        return authenticate(token);
    }

    private boolean mayBeBearerToken(String token) {
        return !isBlank(token) && (token.contains(BEARER_PREFIX) || token.split("\\.").length == 3 && !token.contains(" "));
    }

    private Authentication authenticate(String token) {

        if (!token.contains(BEARER_PREFIX)) {
            token = BEARER_PREFIX + token;
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String url = rainbowApiUrl + "/authentication/v1.0/validator";

        ResponseEntity<ResponseStatus> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, ResponseStatus.class);
        ResponseStatus responseStatus = responseEntity.getBody();
        if (responseStatus == null || !responseStatus.isOk()) {
            return AuthenticatedUser.instanceNotAuthenticatedUser();
        }
        Map map = this.parseToken(token);

        String id = (String) ((Map) map.get("user")).get("id");

        url = rainbowApiUrl + "/admin/v1.0/users/" + id;
        ResponseEntity<AuthenticatedUser> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, AuthenticatedUser.class);
        final AuthenticatedUser authentication = exchange.getBody();
        if (authentication != null) {
            authentication.setToken(token.replace(BEARER_PREFIX, ""));
        }
        return authentication;
    }

    private Map parseToken(String token) {
        if (token == null || token.split("\\.").length != 3) {
            return null;
        }
        int payloadIndex = 1;
        String payload = token.split("\\.")[payloadIndex];

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new String(Base64.decodeBase64(payload.getBytes(StandardCharsets.UTF_8))), Map.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error on token parsing!", e);
        }
    }

}
