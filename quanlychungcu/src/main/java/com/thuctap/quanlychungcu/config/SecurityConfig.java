package com.thuctap.quanlychungcu.config;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String [] KHACHHANG_ROLE={
    };
    private final String [] QUANLY_ROLE={
        "/canho/**",
        "/dichvu/**",
        "/banggia/**"
    };
    private final String [] ADMIN_ROLE={
        "/taikhoan/**"
    };

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request->
        request
        // .requestMatchers(HttpMethod.GET,PUBLIC_ENDPOINTS).hasRole("quanly")//hasAuthority("ROLE_quyen")
        // .requestMatchers(HttpMethod.POST, "/*/*").permitAll()
        // .requestMatchers(HttpMethod.GET, "/canho/loaiphong").permitAll()
        // .requestMatchers(HttpMethod.GET,"/hoadon/**").hasAnyRole("khachhang","quanly")
        
        .requestMatchers(HttpMethod.GET, QUANLY_ROLE).hasRole("quanly")
        .requestMatchers(HttpMethod.POST, QUANLY_ROLE).hasRole("quanly")
        .requestMatchers(HttpMethod.PUT, QUANLY_ROLE).hasRole("quanly")
        .requestMatchers(HttpMethod.DELETE, QUANLY_ROLE).hasRole("quanly")

        .requestMatchers(HttpMethod.GET, ADMIN_ROLE).hasRole("admin")
        .requestMatchers(HttpMethod.POST, ADMIN_ROLE).hasRole("admin")
        .requestMatchers(HttpMethod.PUT, ADMIN_ROLE).hasRole("admin")
        .requestMatchers(HttpMethod.DELETE, ADMIN_ROLE).hasRole("admin")

        .requestMatchers(
            new AntPathRequestMatcher("/*"),
            new AntPathRequestMatcher("/*/*"),
            new AntPathRequestMatcher("/*/*/*"),
            new AntPathRequestMatcher("/*/*/*/*"),
            new AntPathRequestMatcher("/*/*/*/*/*")).permitAll()
            
        .anyRequest().authenticated());
        
        http.oauth2ResourceServer(oauth2->
            oauth2.jwt(jwtConfigurer->jwtConfigurer.decoder(jwtDecoder())
            .jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );
        
        //Tắt csrf
        http.csrf(AbstractHttpConfigurer::disable);
        
        return http.build();
    }

    //Đổi prefix của SCOPE trong token
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    //Mã hóa mật khẩu
    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(),"HS512");
        NimbusJwtDecoder nimbusJwtDecoder =NimbusJwtDecoder
        .withSecretKey(secretKeySpec)
        .macAlgorithm(MacAlgorithm.HS512)
        .build();
        return nimbusJwtDecoder;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}


