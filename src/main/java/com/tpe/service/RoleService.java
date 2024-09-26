package com.tpe.service;

import com.tpe.domain.Role;
import com.tpe.domain.enums.RoleType;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getRoleByType(RoleType roleAdmin) {

        return roleRepository.findByType(roleAdmin).
                orElseThrow(()->new ResourceNotFoundException("Role is not found!"));
    }
}
