package storagecloud9.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import storagecloud9.exception.JwtAuthenticationException;
import storagecloud9.service.TokenBlacklistService;

@Slf4j
@Component
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final TokenBlacklistService blacklistService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = tokenProvider.resolveToken(request);

            if (token == null) {
                throw new JwtAuthenticationException("Missing JWT token");
            }

            if (blacklistService.isBlacklisted(token)) {
                throw new JwtAuthenticationException("Token revoked");
            }

            SecurityContextHolder.getContext().setAuthentication(
                    tokenProvider.getAuthentication(token)
            );

        } catch (JwtAuthenticationException ex) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/login") || path.startsWith("/swagger") || path.startsWith("/v3/api-docs");
    }
}
