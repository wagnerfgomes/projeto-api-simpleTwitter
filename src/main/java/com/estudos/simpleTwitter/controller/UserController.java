package com.estudos.simpleTwitter.controller;

import com.estudos.simpleTwitter.controller.dto.CreateUserDto;
import com.estudos.simpleTwitter.entity.Role;
import com.estudos.simpleTwitter.entity.User;
import com.estudos.simpleTwitter.repository.RoleRepository;
import com.estudos.simpleTwitter.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;


@RestController
public class UserController {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @PostMapping("/user")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDto userDto) {
        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        var userFromDb = userRepository.findByUserName(userDto.username());
        if (userFromDb.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setUserName(userDto.username());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.password()));
        user.setRoles(Set.of(basicRole));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
