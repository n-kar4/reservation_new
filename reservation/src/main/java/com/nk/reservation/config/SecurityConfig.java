package com.nk.reservation.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nk.reservation.controller.AuthenticationController;
import com.nk.reservation.utils.RSAKeyProperties;

import ch.qos.logback.classic.Logger;

@Configuration
public class SecurityConfig {

    Logger logger = (Logger) LoggerFactory.getLogger(AuthenticationController.class);

    private final RSAKeyProperties keys;

    public SecurityConfig(RSAKeyProperties rsaKeyProperties) {
        this.keys = rsaKeyProperties;
    }

    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    // String idForEncode = "bcrypt";
    // Map<String, PasswordEncoder> encoderMap = new HashMap<>();
    // encoderMap.put(idForEncode, new BCryptPasswordEncoder());
    // return new DelegatingPasswordEncoder(idForEncode, encoderMap);
    // }

    

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService detailsService) throws Exception {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(detailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/**").permitAll()
                    // .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    // .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                    // .anyRequest().authenticated()
                    );
                http.oauth2ResourceServer()
                    .jwt();
                http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(ex->ex.authenticationEntryPoint(point));
        
        return http.build();

    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.keys.getPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
        JWKSource<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSet);
    }

    // @Bean
    // public JwtAuthenticationConverter jwtAuthenticationConverter() {

    //     JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    //     jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
    //     jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

    //     logger.info("jwtGrantedAuthoritiesConverter: " + jwtGrantedAuthoritiesConverter);

    //     JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    //     jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

    //     logger.info("jwtAuthenticationConverter: " + jwtAuthenticationConverter);

    //     System.out.println("jwtGrantedAuthoritiesConverter: " + jwtGrantedAuthoritiesConverter);

    //     return jwtAuthenticationConverter;
    // }

}
