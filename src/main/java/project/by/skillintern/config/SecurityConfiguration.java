package project.by.skillintern.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.by.skillintern.exceptions.handler.CustomAccessDeniedHandler;
import project.by.skillintern.jwt.JwtFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor()
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    private static final String[] WHITE_LIST_URL = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/auth/**",
            "/vacancy/all",
            "/vacancy/by-filter",
            "/vacancy/detail/*",
            "/profile/**",
            "/internship/all",
            "/news/all",
            "/vacancy/companies",
            "/vacancy/search"
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(WHITE_LIST_URL).permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/responses/create", "/responses/my", "/responses/my/accepted"
                        , "/responses/my/pending", "/responses/my/rejected").hasRole("USER")
                .requestMatchers("/vacancy/add", "/vacancy/my-vacancies", "/vacancy/edit/*"
                        , "/vacancy/delete/*", "/responses/forMyVacancy", "/responses/status/*", "/vacancy/addCompany").hasRole("EMPLOYER")
                .anyRequest().authenticated()
        );

        http.exceptionHandling(exception -> exception
                .accessDeniedHandler(new CustomAccessDeniedHandler())
        );

        http.sessionManagement(req -> req.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authenticationProvider(customAuthenticationProvider);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}