package com.subscription_manager.service;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

@Service
public class GmailTokenService {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public GmailTokenService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    public String getAccessToken(OAuth2AuthenticationToken authenticationToken) {
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                authenticationToken.getAuthorizedClientRegistrationId(),
                authenticationToken.getName()
        );

        if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
            throw new IllegalArgumentException("Access token not found. The OAuth2 client is null or does not contain a token.");
        }

        return authorizedClient.getAccessToken().getTokenValue();
    }

    public String getRefreshToken(OAuth2AuthenticationToken authenticationToken) {
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                authenticationToken.getAuthorizedClientRegistrationId(),
                authenticationToken.getName()
        );

        if (authorizedClient == null) {
            throw new IllegalArgumentException("Refresh token not found. OAuth2 client is null.");
        }

        OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();
        if (refreshToken == null) {
            throw new IllegalArgumentException("No refresh token is available for this user.");
        }

        return refreshToken.getTokenValue();
    }
}
