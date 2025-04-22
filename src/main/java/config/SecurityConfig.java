package config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

	/*
	 * @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	 * http .csrf(csrf -> csrf.disable()) // Disable CSRF for POST from browser
	 * .authorizeHttpRequests(auth -> auth .requestMatchers("/", "/index.html",
	 * "/api/auth/login", "/css/**", "/js/**") .permitAll() .anyRequest()
	 * .authenticated()) .formLogin(login -> login.disable()) // Disable default
	 * login form .httpBasic(httpBasic -> httpBasic.disable()) // Disable HTTP Basic
	 * Auth .logout(logout -> logout.disable()); // Disable logout
	 * 
	 * return http.build(); }
	 */
}
