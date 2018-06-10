package order.management.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import order.management.entity.ApplicationUser;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private AuthenticationManager aum;
	
	private static final String SECRET = "SecretKeyToGenJWTs";
	
	public JWTAuthenticationFilter(AuthenticationManager aum){
		this.aum = aum;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,HttpServletResponse res)
	throws AuthenticationException {
		
		try{
			ApplicationUser cred = new ObjectMapper()
                    .readValue(req.getInputStream(), ApplicationUser.class);
			
			return aum.authenticate(
					new UsernamePasswordAuthenticationToken(
							cred.getUsername(),
							cred.getPassword(),
							new ArrayList<>()
							)
					);
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}
		
	}
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
			FilterChain chain, Authentication auth) throws IOException,ServletException{
		User user = (User) auth.getPrincipal();
		Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("roles", user.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));
		 String token = Jwts.builder()
	                .setSubject(((User) auth.getPrincipal()).getUsername())
	                .setClaims(claims)
	                .setExpiration(new Date(System.currentTimeMillis() + 8640000))
	                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
	                .compact();
	        res.addHeader("Authorization", "Bearer-" + token);
	        res.setStatus(204);
	      
	}



}
