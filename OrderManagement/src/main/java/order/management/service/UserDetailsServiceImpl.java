package order.management.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import order.management.ApplicationUserRepository;
import order.management.entity.ApplicationUser;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	private ApplicationUserRepository applicationUserRepository;
	
	public UserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository){
		this.applicationUserRepository=applicationUserRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String loadByUsername) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		ApplicationUser user = applicationUserRepository.findByUsername(loadByUsername);
		if (user == null) {
            throw new UsernameNotFoundException(loadByUsername);
        }
		List<String> role = new ArrayList<String>(); 
		role.add(user.getUserRole());
		 List<GrantedAuthority> authorities = role.stream()
	                .map(authority -> new SimpleGrantedAuthority(authority))
	                .collect(Collectors.toList());
		
		return new User(user.getUsername(), user.getPassword(),authorities);
	
	}
}
