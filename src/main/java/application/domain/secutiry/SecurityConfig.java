package application.domain.secutiry;

import application.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, userService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/tarefa").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/api/email/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/api/tarefas/listar").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/id/{tarefaId}").hasRole("USER")
                .antMatchers(HttpMethod.GET, "descricao/{descricao}").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/api/tarefas/status/{status}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/tarefas/filtro").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/tarefas/responsavel/{responsavel}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/tarefas/remover/{tarefaId}").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/{tarefaId}").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/{tarefaId}/concluir").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/api/usuarios/admin/{idUsuario}").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/usuarios/**").permitAll()
                .anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint((request, response, e) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token expirado: faça login novamente para obter um novo token!");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Acesso negado: Você não tem permissão para acessar esse serviço!");
                });
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

}
