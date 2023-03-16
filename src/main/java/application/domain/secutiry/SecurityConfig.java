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
                .antMatchers(HttpMethod.POST, "/api/tarefa").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/api/email").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/listar").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/id/{tarefaId}").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/descricao/{descricao}").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/status/{status}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/filtro").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/{tarefaId}").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/{tarefaId}").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.PUT, "/{tarefaId}/concluir").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/api/usuarios/**").permitAll()
                .anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
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
