# CORS Configuration for Spring Boot Backend

To allow the Next.js frontend to communicate with your Spring Boot backend, you need to add CORS configuration.

## Option 1: Add CORS Configuration Class

Create a new file: `src/main/java/com/example/northwind/config/CorsConfig.java`

```java
package com.example.northwind.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://127.0.0.1:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
```

## Option 2: Add CORS to Security Configuration

If you're using Spring Security, update your security configuration to allow CORS:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://127.0.0.1:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
```

## Option 3: Application Properties (Simple)

Add to your `application.yml`:

```yaml
spring:
  web:
    cors:
      allowed-origins: "http://localhost:3000,http://127.0.0.1:3000"
      allowed-methods: "GET,POST,PUT,DELETE,OPTIONS"
      allowed-headers: "*"
      allow-credentials: true
```

## Testing CORS

After adding CORS configuration:

1. Restart your Spring Boot application
2. Start the Next.js frontend: `npm run dev`
3. Open `http://localhost:3000` in your browser
4. Check the browser's Network tab to ensure API calls are successful

## Production Considerations

For production deployment, update the allowed origins to include your production frontend URL:

```java
configuration.addAllowedOrigin("https://your-frontend-domain.com");
```

## Troubleshooting

- **CORS errors in browser**: Check that the origins match exactly
- **Preflight requests failing**: Ensure OPTIONS method is allowed
- **Credentials not sent**: Set `allowCredentials(true)` and ensure origins don't use wildcards
