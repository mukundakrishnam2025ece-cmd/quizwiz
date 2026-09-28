package com.example.quizwiz.security;

import com.example.quizwiz.repository.FacultyRepository;
import com.example.quizwiz.repository.StudentRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
        public UserDetailsService userDetailsService(FacultyRepository facultyRepository,
                            StudentRepository studentRepository) {
        return username -> {
            if (username.startsWith("STUDENT:")) {
            String studentCode = username.substring("STUDENT:".length());
            return studentRepository.findByStudentCodeIgnoreCase(studentCode)
                .filter(student -> student.getPasswordHash() != null)
                .map(student -> User.withUsername("STUDENT:" + student.getStudentCode())
                    .password(student.getPasswordHash())
                    .roles("STUDENT")
                    .build())
                .orElseThrow(() -> new UsernameNotFoundException("Student account not found."));
            }
            return facultyRepository.findByEmailIgnoreCase(username)
                .map(faculty -> User.withUsername(faculty.getEmail())
                    .password(faculty.getPasswordHash())
                    .roles("FACULTY")
                    .build())
                .orElseThrow(() -> new UsernameNotFoundException("Faculty account not found."));
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository) throws Exception {
        HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
        csrfTokenRepository.setHeaderName("X-CSRF-TOKEN");

        http
                .securityContext(context -> context.securityContextRepository(securityContextRepository))
                .csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/index.html", "/favicon.ico", "/error").permitAll()
                        .requestMatchers("/api/auth/csrf", "/api/auth/student/register", "/api/auth/student/login", "/api/auth/faculty/register", "/api/auth/faculty/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/quizzes", "/api/quizzes/*/questions").hasRole("FACULTY")
                        .requestMatchers(HttpMethod.DELETE, "/api/quizzes/*").hasRole("FACULTY")
                        .requestMatchers(HttpMethod.GET, "/api/quizzes/mine").hasRole("FACULTY")
                        .requestMatchers(HttpMethod.GET, "/api/attempts/quiz/*/results").hasRole("FACULTY")
                        .requestMatchers("/api/auth/faculty/me", "/api/auth/faculty/logout").hasRole("FACULTY")
                        .requestMatchers("/api/auth/student/me", "/api/auth/student/logout").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/quizzes", "/api/quizzes/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/quizzes/*/questions", "/api/questions/*").hasAnyRole("STUDENT", "FACULTY")
                        .requestMatchers(HttpMethod.POST, "/api/students").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/students").hasRole("FACULTY")
                        .requestMatchers(HttpMethod.GET, "/api/students/*").hasAnyRole("STUDENT", "FACULTY")
                        .requestMatchers("/api/attempts/start", "/api/attempts/*/submit", "/api/attempts/*").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/attempts").hasAnyRole("STUDENT", "FACULTY")
                        .anyRequest().denyAll())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout.disable())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) -> writeAuthError(
                    response, HttpServletResponse.SC_UNAUTHORIZED, "Sign-in required."))
                        .accessDeniedHandler((request, response, exception) -> writeAuthError(
                                response, HttpServletResponse.SC_FORBIDDEN, "Faculty access required.")));

        return http.build();
    }

    private void writeAuthError(HttpServletResponse response, int status, String message)
            throws java.io.IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"status\":\"error\",\"message\":\"" + message + "\"}");
    }
}