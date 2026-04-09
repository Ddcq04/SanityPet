package com.veterinaria.gestion.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
	             .requestMatchers("/css/**", "/js/**", "/images/**","/logo/**","/portada/**").permitAll() // Permite cargar estilos sin login
	          // 2. GESTIÓN DE CLIENTES: Solo el administrador puede crear, buscar o listar dueños
	             .requestMatchers("/clientes/**").hasRole("admin")
	          // 3. MASCOTAS: 
	                // El administrador puede ver todas, y el usuario puede ver "mis-mascotas"d)
	             // 1. Permitir que TODOS los logueados vean y creen sus mascotas
	                .requestMatchers("/mascotas/mis-mascotas", "/mascotas/nueva", "/mascotas/guardar").hasAnyRole("user", "admin")
	                // 2. Solo el ADMIN puede ver el listado global o borrar
	                .requestMatchers("/mascotas/**").hasRole("admin")
	          // 4. Citas: 
	                .requestMatchers("/citas/**").hasAnyRole("admin", "user")
	          // 5. Tienda Virtual:
	                // Los administradores pueden gestionar productos
	                .requestMatchers("/tienda/nuevo", "/tienda/guardar", "/tienda/editar/**", "/tienda/eliminar/**").hasRole("admin")
	                // Cualquier autenticado puede ver la tienda
	                .requestMatchers("/tienda").hasAnyRole("admin", "user", "vet")
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
	       return new BCryptPasswordEncoder(); // Para manejar contraseñas seguras
	    }
	}
