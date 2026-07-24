package mx.egd.fmre.register.service.impl;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.record.OpenIdConfiguration;
import mx.egd.fmre.register.record.UserInfo;
import mx.egd.fmre.register.service.UserinfoService;

@Service
@RequiredArgsConstructor
public class UserinfoServiceImpl implements UserinfoService {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String oauth2ResourceserverJwtIssuerUri;

    private final RestClient.Builder restClientBuilder;

    private static final String WELL_KNOW_OPENID_CONFIGURATION = "/.well-known/openid-configuration";
    private static final String BEARER_ = "Bearer ";
    private static final String EMPTY_STRING = "";

    @Override
    public String getUsernameFromToken(String token) {
        if(token == null){
            return null;
        }
        RestClient restClient = restClientBuilder.build();
        
        @Nullable OpenIdConfiguration openIdConfiguration = restClient.get()
                .uri(oauth2ResourceserverJwtIssuerUri + WELL_KNOW_OPENID_CONFIGURATION)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(OpenIdConfiguration.class);
        if(openIdConfiguration == null || openIdConfiguration.userinfoEndpoint() == null) {
            return null;
        }
        
        String userinfoEndpoint = openIdConfiguration.userinfoEndpoint();        
        @Nullable
        UserInfo userInfo = restClient.get()
                .uri(userinfoEndpoint)
                .headers(headers -> headers.setBearerAuth(token.replaceAll(BEARER_, EMPTY_STRING)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(UserInfo.class);
        if(userInfo == null || userInfo.email() == null) {
            return null;
        }
        return userInfo.email();
    }
}
