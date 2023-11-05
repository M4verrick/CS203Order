package eztix.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/v1/**").hasAnyRole("user", "admin")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/**").hasRole("admin")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/**").hasRole("admin")

                        .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "**").denyAll()
                )
                .oauth2ResourceServer(oauth2Configurer -> oauth2Configurer.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter((jwt -> {
                    Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
                    Collection<String> roles = realmAccess.get("roles");
                    var grantedAuthorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .toList();

                    return new JwtAuthenticationToken(jwt, grantedAuthorities);

                }))));

        return http.build();
    }
}
