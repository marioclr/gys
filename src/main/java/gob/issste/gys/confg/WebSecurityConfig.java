package gob.issste.gys.confg;

import gob.issste.gys.service.SecurityService;
import gob.issste.gys.security.JWTAuthorizationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    JWTAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of(
                        SecurityService.LOCAL_SERVER_URL,
                        SecurityService.DEV_SERVER_URL,
                        SecurityService.PROD_SERVER_URL
                ));
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                return configuration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher(SecurityService.API_URL)
                        ).authenticated()
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, SecurityService.LOGIN_URL),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET, SecurityService.KEY_URL))
                        .permitAll())
                .headers(headers -> headers
                        .addHeaderWriter(
                                new StaticHeadersWriter(
                                        "Access-Control-Allow-Origin",
                                        "https://sigysdev.issste.gob.mx," +
                                                    "https://sigys.issste.gob.mx," +
                                                    "http://localhost:4200"))
                        .addHeaderWriter(
                                new StaticHeadersWriter(
                                         "Access-Control-Max-Age", "3600"))
                        .addHeaderWriter(
                                new StaticHeadersWriter(
                                         "Access-Control-Allow-Credentials", "true"))
                        .addHeaderWriter(
                                new StaticHeadersWriter(
                                         "Access-Control-Allow-Headers",
                                        "Origin,Accept,X-Requested-With,Content-Type," +
                                                    "Access-Control-Request-Method,Access-Control-Request-Headers,Authorization")))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                        }))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
