package com.example.taskflow.Service;

import com.example.taskflow.Config.KeycloakAdminProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@EnableConfigurationProperties(KeycloakAdminProperties.class)
public class KeycloakAdminService {

    private final KeycloakAdminProperties props;
    private final RestClient restClient;

    public KeycloakAdminService(KeycloakAdminProperties props) {
        this.props = props;
        this.restClient = RestClient.create();
    }

    public String createUser(String name, String email) {
        String adminToken = fetchServiceAccountToken();

        String[] nameParts = name.trim().split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        Map<String, Object> newUser = Map.of(
                "username", email,
                "email", email,
                "firstName", firstName,
                "lastName", lastName,
                "enabled", true,
                "emailVerified", true,
                "requiredActions", List.of("UPDATE_PASSWORD"));

        // Build safe absolute URI
        String userEndpoint = UriComponentsBuilder.fromUriString(
            Objects.requireNonNull(props.serverUrl(), "serverUrl must not be null"))
                .path("/admin/realms/{realm}/users")
                .buildAndExpand(props.realm())
                .toUriString();

        ResponseEntity<Void> response = restClient.post()
                .uri(userEndpoint)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(newUser)
                .retrieve()
                .toBodilessEntity();

        String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        if (location == null) {
            throw new IllegalStateException("Keycloak did not return a Location header for the created user");
        }
        return location.substring(location.lastIndexOf('/') + 1);
    }

    private String fetchServiceAccountToken() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", props.clientId());
        form.add("client_secret", props.clientSecret());

        // Build safe absolute URI to prevent 'URI with undefined scheme'
        String tokenEndpoint = UriComponentsBuilder.fromUriString(
            Objects.requireNonNull(props.serverUrl(), "serverUrl must not be null"))
                .path("/realms/{realm}/protocol/openid-connect/token")
                .buildAndExpand(props.realm())
                .toUriString();

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restClient.post()
                .uri(tokenEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Map.class);

        if (response == null || !response.containsKey("access_token")) {
            throw new IllegalStateException("Failed to retrieve access token from Keycloak response");
        }

        return (String) response.get("access_token");
    }
}