package mx.egd.fmre.register.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class PostaliaRestClientConfig {

	@Value("${postalia.api}")
	private String token;

	@Bean
	RestClient postaliaRestClient(RestClient.Builder builder) {
		return builder.baseUrl("https://postalia.com.mx")
				.defaultHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}
}
