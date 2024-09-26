package com.tpe.security;

import com.tpe.domain.Role;
import com.tpe.domain.User;
import com.tpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //amaç: UserDetails <--> User, GrantedAuthority<-->Role

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user=userRepository.findByUserName(username).
                orElseThrow(()->new UsernameNotFoundException("User not found by username:"+username));

        return new org.springframework.security.core.userdetails.
                User(user.getUserName(),user.getPassword(),buildGrantedAuthorities(user.getRoles()));
        //kendi userımızın bilgileri ile securitynin userını(userdetails) oluşturduk
    }

    //bizim userımızın rolleri var -->grantedauthoritylere çevirelim
    //simplegrantedauthority->security nin tanıdığı rolleri
    private List<SimpleGrantedAuthority> buildGrantedAuthorities(Set<Role> roles){
        List<SimpleGrantedAuthority> authorities=new ArrayList<>();

        for (Role role:roles){

            authorities.add(new SimpleGrantedAuthority(role.getType().name()));//new SimpleGrantedAuthority(ROLE_ADMIN)

        }
        //SimpleGrantedAuthority nin constructorına parametre olarak
        //rollerimizin type nın name ini vererek rollerden grantedauthority elde ettik.
        return authorities;
    }
}
