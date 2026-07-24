package mx.egd.fmre.register.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record OpenIdConfiguration(
    @JsonProperty("authorization_endpoint") 
    String authorizationEndpoint,
    
    @JsonProperty("end_session_endpoint") 
    String endSessionEndpoint,
    
    @JsonProperty("id_token_signing_alg_values_supported") 
    List<String> idTokenSigningAlgValuesSupported,
    
    @JsonProperty("issuer") 
    String issuer,
    
    @JsonProperty("jwks_uri") 
    String jwksUri,
    
    @JsonProperty("response_types_supported") 
    List<String> responseTypesSupported,
    
    @JsonProperty("revocation_endpoint") 
    String revocationEndpoint,
    
    @JsonProperty("scopes_supported") 
    List<String> scopesSupported,
    
    @JsonProperty("subject_types_supported") 
    List<String> subjectTypesSupported,
    
    @JsonProperty("token_endpoint") 
    String tokenEndpoint,
    
    @JsonProperty("token_endpoint_auth_methods_supported") 
    List<String> tokenEndpointAuthMethodsSupported,
    
    @JsonProperty("userinfo_endpoint") 
    String userinfoEndpoint
) {}