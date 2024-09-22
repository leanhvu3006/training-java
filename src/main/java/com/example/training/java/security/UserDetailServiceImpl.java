package com.example.training.java.security;

import java.util.ArrayList;
import java.util.List;

import com.example.training.java.entitys.CustomerEntity;
import com.example.training.java.repositorys.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            final CustomerEntity customerEntity = customerRepository.findByUsername(username).orElseThrow(Exception::new);
            List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(customerEntity.getRole()); //customerEntity.getRole() == ADMIN
            grantedAuthorityList.add(grantedAuthority);
            return new User(username, customerEntity.getPassword(), grantedAuthorityList);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Login fail with user name: " + username);
        }
    }
}
