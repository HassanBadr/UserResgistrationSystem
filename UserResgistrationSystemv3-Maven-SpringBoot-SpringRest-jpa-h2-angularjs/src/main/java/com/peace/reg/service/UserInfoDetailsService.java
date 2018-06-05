package com.peace.reg.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.peace.reg.dto.UserInfo;
import com.peace.reg.repository.UserInfoJpaRepository;

@Service
public class UserInfoDetailsService implements UserDetailsService {

	@Autowired
	private UserInfoJpaRepository userInfoJpaRepository; 
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserInfo user = userInfoJpaRepository.findByUserName(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("Opps user not found with user-name" + username);
		}
		
		return new User(user.getUserName(), user.getPassword(), getAuthorties(user));
	}
	
	
	private Collection<GrantedAuthority> getAuthorties(UserInfo user){
		
		List<GrantedAuthority> authorties = new ArrayList<>();
		authorties = AuthorityUtils.createAuthorityList(user.getRole());
		
		return authorties;
		
	}

}
