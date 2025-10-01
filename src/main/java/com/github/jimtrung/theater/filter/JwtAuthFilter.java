package com.github.jimtrung.theater.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.jimtrung.theater.util.AuthTokenUtil;

import java.io.IOException;
import java.util.UUID;

public class JwtAuthFilter extends OncePerRequestFilter {
  private final AuthTokenUtil authTokenUtil;

  public JwtAuthFilter(AuthTokenUtil authTokenUtil) {
    this.authTokenUtil = authTokenUtil;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return !path.startsWith("/user/");
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);

      try {
        UUID userId = authTokenUtil.parseToken(token);
        request.setAttribute("userId", userId);
      } catch (io.jsonwebtoken.ExpiredJwtException e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("TOKEN_EXPIRED");
        return;
      } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("INVALID_TOKEN");
        return;
      }
    } else {
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("MISSING_TOKEN");
        return;
      }
    }
    filterChain.doFilter(request, response);
  }
}
