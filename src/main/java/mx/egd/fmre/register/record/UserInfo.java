package mx.egd.fmre.register.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserInfo(
    @JsonProperty("sub") 
    String sub,
    
    @JsonProperty("email_verified") 
    String emailVerified,
    
    @JsonProperty("email") 
    String email,
    
    @JsonProperty("username") 
    String username
) {}