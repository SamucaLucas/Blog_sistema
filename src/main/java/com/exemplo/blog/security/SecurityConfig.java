package com.exemplo.blog.security; 
  
import com.exemplo.blog.service.UsuarioDetailsService; 
import lombok.RequiredArgsConstructor; 
import org.springframework.context.annotation.*; 
import org.springframework.security.authentication.*; 
import 
org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity; 
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; 
import org.springframework.security.config.http.SessionCreationPolicy; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.security.web.SecurityFilterChain; 
import org.springframework.web.cors.CorsConfiguration; 
import org.springframework.web.cors.CorsConfigurationSource; 
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; 
  
import java.util.List; 
  
@Configuration 
@EnableWebSecurity 
@RequiredArgsConstructor 
public class SecurityConfig { 
  
    private final UsuarioDetailsService usuarioDetailsService; 
  
    @Bean 
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    } 
  
    @Bean 
    public AuthenticationManager authManager(AuthenticationConfiguration cfg) throws Exception { 
        return cfg.getAuthenticationManager(); 
    } 
  
    @Bean 
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
        http 
            .cors(c -> c.configurationSource(corsConfig())) 
            .csrf(csrf -> csrf.disable()) 
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) 
            .authorizeHttpRequests(auth -> auth 
                .requestMatchers("/api/auth/**").permitAll() 
                .requestMatchers("/api/posts/**").permitAll()  // leitura pública 
                .requestMatchers("/api/usuarios/**").permitAll() 
                .requestMatchers("/", "/static/**", "/*.html", "/css/**", "/js/**") 
                    .permitAll() 
                .requestMatchers("POST", "/api/posts").authenticated() 
                .requestMatchers("PUT", "/api/posts/**").authenticated() 
                .requestMatchers("DELETE", "/api/posts/**").authenticated() 
                .requestMatchers("/api/comentarios/**").authenticated() 
                .anyRequest().authenticated() 
            ) 
            .userDetailsService(usuarioDetailsService) 
            .formLogin(f -> f.disable()) 
            .httpBasic(b -> b.disable()); 
        return http.build(); 
    } 
  
    private CorsConfigurationSource corsConfig() { 
        CorsConfiguration cfg = new CorsConfiguration(); 
        cfg.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:3000")); 
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE")); 
        cfg.setAllowedHeaders(List.of("*")); 
        cfg.setAllowCredentials(true); 
        var source = new UrlBasedCorsConfigurationSource(); 
        source.registerCorsConfiguration("/**", cfg); 
        return source; 
    } 
}