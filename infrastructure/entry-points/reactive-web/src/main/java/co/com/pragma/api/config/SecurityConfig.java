package co.com.pragma.api.config;

import co.com.pragma.api.jwt.JwtProvider;
import co.com.pragma.api.jwt.SecurityContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import org.springframework.security.web.server.SecurityWebFilterChain;




@RequiredArgsConstructor
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtFilter jwtFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges.pathMatchers("/api/v1/users").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.FIRST)
                .securityContextRepository(securityContextRepository)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }
}

//    @Bean
//    public ReactiveAuthenticationManager authenticationManager() {
//        return authentication -> {
//            String authToken = authentication.getCredentials().toString();
//
//            // Usamos nuestro JwtProvider para validar y obtener los datos
//            System.out.println(authToken);
//            if (jwtProvider.validate(authToken)) {
//                var claims = jwtProvider.getClaims(authToken);
//                var roles = (List<String>) claims.get("roles");
//                var authorities = roles.stream()
//                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Spring necesita el prefijo ROLE_
//                        .collect(Collectors.toList());
//                System.out.println(authorities.toString());
//
//                String username = jwtProvider.getSubject(authToken); // Obtenemos el ID del usuario
//
//                // Creamos el objeto de autenticación que Spring Security usará internamente
//                var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
//                return Mono.just(auth);
//            } else {
//                // Si el token no es válido, retornamos un Mono vacío para rechazar la autenticación
//                System.out.println("fallo");
//                return Mono.empty();
//            }
//        };
    //}

//    @Bean
//    public ServerSecurityContextRepository securityContextRepository() {
//        return new ServerSecurityContextRepository() {
//
//
//            @Override
//            public Mono<Void> save(ServerWebExchange exchange, org.springframework.security.core.context.SecurityContext context) {
//                return null;
//            }
//
//            @Override
//            public Mono<SecurityContext> load(ServerWebExchange exchange) {
//                // Buscamos el token en la cabecera Authorization
//                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//                if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                    String authToken = authHeader.substring(7); // Extraemos el token sin "Bearer "
//                    var auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
//
//                    // Le pasamos el token a nuestro AuthenticationManager para que lo valide
//                    return authenticationManager().authenticate(auth).map(SecurityContextImpl::new);
//                }
//                return Mono.empty(); // Si no hay token, no hay contexto de seguridad
//            }
//        };

