package cat.copernic.AnnaSerrano.EntreBicisAnna.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuració de seguretat de l'aplicació mitjançant Spring Security.
 *
 * Defineix les rutes públiques i protegides, la configuració del formulari de
 * login,
 * la gestió de sessió, el sistema de logout i la codificació de contrasenyes.
 */
@Configuration
public class SecurityConfig {
        @Autowired
        private ValidadorUsuaris validadorUsuaris;

        /**
         * Defineix la cadena de filtres de seguretat per HTTP.
         *
         * Inclou la configuració de les rutes públiques, el login personalitzat,
         * la gestió de sessió, l'accés restringit per rol i la gestió d'errors.
         *
         * @param http objecte de configuració de seguretat HTTP.
         * @return cadena de filtres de seguretat configurada.
         * @throws Exception si hi ha errors en la configuració.
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable()) // Per api rest
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/**", "/css/**", "/imatges/**", "/js/**")
                                                .permitAll() // ✅ Ara permet aquests arxius
                                                // .requestMatchers("/registre/client-alta").permitAll()
                                                .requestMatchers("/login").permitAll()
                                                .requestMatchers("/llistarRutes").hasAnyRole("ADMIN")
                                                .anyRequest().authenticated()) // El resto requiere autenticación

                                .formLogin(login -> login
                                                .loginPage("/login") // Página de login personalizada
                                                .defaultSuccessUrl("/", true) // Redirigir a "/" tras login exitoso
                                                .failureUrl("/login?error=true") // Redirigir a login con error si
                                                                                 // fallan las credenciales
                                                .usernameParameter("email") // Nombre del campo de usuario
                                                .passwordParameter("contrasenya") // Nombre del campo de contraseña
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true") // Redirigir a login tras logout
                                                .invalidateHttpSession(true) // Invalida la sesión HTTP
                                                .deleteCookies("JSESSIONID") // Elimina las cookies de sesión
                                                .permitAll())

                                .exceptionHandling(handling -> handling
                                                .accessDeniedPage("/errorPermisos"));
                ;

                return http.build();
        }

        /**
         * Defineix el codificador de contrasenyes que utilitza l'algoritme BCrypt.
         *
         * @return instància de PasswordEncoder per a codificar i verificar
         *         contrasenyes.
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
                // return NoOpPasswordEncoder.getInstance(); // Codificación de contraseñas
        }

        /**
         * Configura el gestor d'autenticació per a l'aplicació.
         *
         * Utilitza el servei `UserDetailsService` personalitzat i el codificador de
         * contrasenyes.
         *
         * @param http objecte de configuració de seguretat HTTP.
         * @return instància d'AuthenticationManager configurada.
         * @throws Exception si hi ha errors en la construcció.
         */
        @SuppressWarnings("removal")
        @Bean
        public AuthenticationManager authManager(HttpSecurity http) throws Exception {
                return http.getSharedObject(AuthenticationManagerBuilder.class)
                                .userDetailsService(validadorUsuaris)
                                .passwordEncoder(passwordEncoder())
                                .and()
                                .build();
        }

}
