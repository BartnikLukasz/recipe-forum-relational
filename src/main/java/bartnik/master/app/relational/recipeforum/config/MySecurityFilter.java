package bartnik.master.app.relational.recipeforum.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class MySecurityFilter extends OncePerRequestFilter {

    private final String PARAMETER_LOGIN = "username";
    private final String PARAMETER_PASSWORD = "password";

    private final AuthenticationManager authenticationManager;

    public MySecurityFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("Before filterchain");

        if (!Collections.list(request.getParameterNames()).contains(PARAMETER_LOGIN) || !Collections.list(request.getParameterNames()).contains(PARAMETER_PASSWORD)) {
            System.out.println("No Parameters!");
            filterChain.doFilter(request, response);
            return;
        }
        var login = request.getParameter(PARAMETER_LOGIN);
        var password = request.getParameter(PARAMETER_PASSWORD);
        var authRequest = MySecurityAuthentication.unauthenticated(login, password);
        try {
            var authentication =  authenticationManager.authenticate(authRequest);
            var newContext = SecurityContextHolder.createEmptyContext();
            newContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(newContext);
            //filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().println("Invalid credentials!");
        }

        System.out.println("After filterchain\n");
    }
}
