package com.sh.global.oauth;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

// OAuthApiClient 를 사용하는 Service 클래스
@Component
public class RequestOAuthInfoService {

    private final Map<OAuthProvider, OAuthApiClient> clients;

    public RequestOAuthInfoService(List<OAuthApiClient> clients) {
        this.clients =
                clients.stream()
                        .collect(
                                Collectors.toUnmodifiableMap(
                                        OAuthApiClient::oAuthProvider, Function.identity()));
    }

    public OAuthInfoResponse request(OAuthLoginParams params) {
        OAuthApiClient client = clients.get(params.oAuthProvider());
        String accessToken = client.requestAccessToken(params);

        return client.requestOauthInfo(accessToken);
    }

    public Long requestLogout(OAuthProvider provider, Long oauthId) {
        OAuthApiClient client = clients.get(provider);
        OAuthLogoutResponse response = client.requestOauthLogout(oauthId);

        return response.getOAuthProviderId();
    }
}
