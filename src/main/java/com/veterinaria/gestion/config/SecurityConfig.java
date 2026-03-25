package com.veterinaria.gestion.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
	         .csrf(csrf -> csrf.disable())
	         .authorizeHttpRequests(auth -> auth
	             .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Permite cargar estilos sin login
	          // 2. GESTIÓN DE CLIENTES: Solo el administrador puede crear, buscar o listar dueños
	             .requestMatchers("/clientes/**").hasRole("admin")
	          // 3. MASCOTAS: 
	                // El administrador puede ver todas, y el usuario puede ver "mis-mascotas"
	                // Usamos hasAnyRole para permitir a ambos entrar en este bloque
	                .requestMatchers("/mascotas/mis-mascotas").hasAnyRole("user", "admin")
	                .requestMatchers("/mascotas/**").hasRole("admin")
	          // 4. Citas: 
	                .requestMatchers("/citas/**").hasAnyRole("admin", "user")
	             .anyRequest().authenticated() // Todo lo demás requiere login
	         )
	         .formLogin(form -> form
	              .loginPage("/login") // Define tu propia ruta de login
	              .defaultSuccessUrl("/home", true) // A donde va tras loguearse con éxito
	              .permitAll()
	            )
	            .logout(logout -> logout.permitAll());

	        return http.build();
	    }
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	    	return NoOpPasswordEncoder.getInstance();
	       // return new BCryptPasswordEncoder(); // Para manejar contraseñas seguras
	    }
	}
