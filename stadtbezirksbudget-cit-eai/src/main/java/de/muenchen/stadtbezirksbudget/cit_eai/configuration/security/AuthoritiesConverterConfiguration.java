package de.muenchen.stadtbezirksbudget.cit_eai.configuration.security;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configure how authorities are resolved.
 * Either {@link KeycloakRolesAuthoritiesConverter} or with profile "userinfo-authorities"
 * deprecated {@link UserInfoAuthoritiesConverter}.
 */
@Configuration
@Profile("!no-security")
public class AuthoritiesConverterConfiguration {
    /**
     * Creates a KeycloakRolesAuthoritiesConverter bean.
     *
     * @param securityProperties the security properties
     * @return the KeycloakRolesAuthoritiesConverter
     */
    @Bean
    @Profile("!userinfo-authorities")
    public KeycloakRolesAuthoritiesConverter keycloakRolesAuthoritiesConverter(final SecurityProperties securityProperties) {
        return new KeycloakRolesAuthoritiesConverter(securityProperties);
    }

    /**
     * Creates a UserInfoAuthoritiesConverter bean.
     *
     * @param securityProperties the security properties
     * @param restTemplateBuilder the rest template builder
     * @return the UserInfoAuthoritiesConverter
     */
    @Bean
    @Profile("userinfo-authorities")
    public UserInfoAuthoritiesConverter userInfoAuthoritiesConverter(
            final SecurityProperties securityProperties, final RestTemplateBuilder restTemplateBuilder) {
        return new UserInfoAuthoritiesConverter(securityProperties.getUserInfoUri(), restTemplateBuilder);
    }
}
