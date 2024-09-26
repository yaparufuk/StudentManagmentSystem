package com.tpe.service;

import com.tpe.domain.Role;
import com.tpe.domain.User;
import com.tpe.domain.enums.RoleType;
import com.tpe.dto.UserRequest;
import com.tpe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public void saveUser(UserRequest userRequest) {//12345678

        User  user=new User();
        user.setFirstName(userRequest.getFirstName());
        user.setUserName(userRequest.getUserName());
        //passwordü şifreleyerek DB ye kaydedelim
        String password= userRequest.getPassword();
        String encodedPassword=passwordEncoder.encode(password);
        //DTO daki password şifrelendi
        user.setPassword(encodedPassword);

        //rolünün verilmesi
        Set<Role> roleSet=new HashSet<>();
        Role role=roleService.getRoleByType(RoleType.ROLE_ADMIN);
        //NOT:Genelde kullanıcılara default olarak en düşük yetkideki rol verilir.
        roleSet.add(role);
        user.setRoles(roleSet);

        userRepository.save(user);
    }

}
