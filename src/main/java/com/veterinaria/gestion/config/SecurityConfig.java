package com.veterinaria.gestion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clase de configuración de seguridad encargada de establecer el control de acceso,
 * los privilegios de los usuarios mediante roles y el proceso de autenticación del sistema.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitación de CSRF de forma temporal para simplificar las peticiones POST/PUT durante el desarrollo
            .csrf(csrf -> csrf.disable())
            
            // Configuración del filtrado de solicitudes y reglas de autorización
            .authorizeHttpRequests(auth -> auth
                // 1. Recursos públicos: Acceso libre a la página principal, estáticos y flujos de registro
                .requestMatchers("/", "/home", "/css/**", "/js/**", "/especialistas/**", "/logo/**", 
                                 "/portada/**", "/img/**", "/favicons/**", "/registrarse", 
                                 "/registrar-socio", "/politicas/**").permitAll()
                                 
                // 2. Módulo de Clientes: Acceso exclusivo para el perfil de administración
                .requestMatchers("/clientes/**").hasRole("admin")
                
                // 3. Módulo de Mascotas: 
                // Permite el acceso a los propietarios para la gestión ordinaria de sus animales y al administrador
                .requestMatchers("/mascotas/mis-mascotas", "/mascotas/nueva", "/mascotas/guardar").hasAnyRole("user", "admin")
                // Restringe las operaciones globales (listados completos, borrados directos) únicamente al administrador
                .requestMatchers("/mascotas/**").hasRole("admin")
                
                // 4. Módulo de Citas: Acceso compartido para usuarios registrados y administración
                .requestMatchers("/citas/**").hasAnyRole("admin", "user")
                
                // 5. Módulo de Tienda Virtual:
                // Restricción para las operaciones de mantenimiento de productos (CRUD)
                .requestMatchers("/tienda/nuevo", "/tienda/guardar", "/tienda/editar/**", "/tienda/eliminar/**").hasRole("admin")
                // Permiso de lectura de catálogo y gestión del carrito para cualquier rol autenticado en la plataforma
                .requestMatchers("/tienda", "/tienda/carrito/**").hasAnyRole("admin", "user", "vet")
                
                // Control subsidiario: Cualquier otra ruta no especificada requerirá autenticación previa
                .anyRequest().authenticated()
            )
            
            // Configuración del sistema de autenticación basada en formulario
            .formLogin(form -> form
                .loginPage("/login")               // Ruta del controlador que renderiza la vista personalizada de login
                .loginProcessingUrl("/login")       // Endpoint interno expuesto por Spring Security para procesar las credenciales
                .defaultSuccessUrl("/home", true)   // Redirección forzada tras completar un inicio de sesión válido
                .permitAll()                        // Permite acceso libre a la pantalla para gestionar parámetros de error o estados
            )
            
            // Configuración del proceso de cierre de sesión
            .logout(logout -> logout.permitAll())
            
            // Configuración del mecanismo de persistencia de sesión ("Recuérdame")
            .rememberMe(remember -> remember
                .key("uniqueAndSecretKey")          // Clave privada utilizada para la firma criptográfica de la cookie de sesión
                .tokenValiditySeconds(86400)        // Ciclo de vida estipulado para la cookie (24 horas)
            );

        return http.build();
    }

    /**
     * Definición del mecanismo de codificación de contraseñas de la aplicación.
     * Actualmente configurado en texto plano (NoOp) para facilitar las pruebas de desarrollo,
     * preparado para su transición a esquemas robustos como BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // NOTA: Implementación provisional para pruebas en entorno local con datos de testeo.
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        
        // Entorno de producción (descomentar al desplegar):
        // return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
}
