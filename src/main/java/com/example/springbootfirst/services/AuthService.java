package com.example.springbootfirst.services;

import com.example.springbootfirst.models.RegisterDetails;
import com.example.springbootfirst.models.Roles;
import com.example.springbootfirst.models.UserDetailsDto;
import com.example.springbootfirst.repository.RegisterDetailsRepository;
import com.example.springbootfirst.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    RegisterDetailsRepository registerDetailsRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public String addNewEmployee(UserDetailsDto register) {
        RegisterDetails registerDetails = new RegisterDetails();
        registerDetails.setEmpId(register.getEmpId());
        registerDetails.setName(register.getName());
        registerDetails.setEmail(register.getEmail());
        registerDetails.setPassword(passwordEncoder.encode(register.getPassword()));
        registerDetails.setUserName(register.getUserName());
        Set<Roles> roles = new HashSet<>();
        for(String roleName: register.getRoleNames()){
            Roles role = rolesRepository.findByRoleName(roleName)
                    .orElseThrow(()->new RuntimeException("User not found" + roleName));
            roles.add(role);
        }
        registerDetails.setRoles(roles);
        System.out.println("Registration"+ registerDetails);
        registerDetailsRepository.save(registerDetails);
        return "Employee Added Successfully";
    }

    public String authenticate(RegisterDetails login) {
        RegisterDetails user = registerDetailsRepository.findByEmail(login.getEmail());
        if(user != null){
            if (passwordEncoder.matches(login.getPassword(),user.getPassword())){
                return "Login Successful";
            }
        }
        return "Login Not Successful";
    }
}
