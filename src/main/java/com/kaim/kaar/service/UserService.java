package com.kaim.kaar.service;

import com.kaim.kaar.DTOs.LoginResponseDTO;
import com.kaim.kaar.DTOs.RegisterResponseDTO;
import com.kaim.kaar.config.JwtUtil;
import com.kaim.kaar.entity.User;
import com.kaim.kaar.repository.UserRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {


    @Autowired
    private UserRepositoy userRepositoy;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public UserService(UserRepositoy userRepositoy) {
        this.userRepositoy = userRepositoy;
    }

    public RegisterResponseDTO register(User user){
        User userByEmail = userRepositoy.findByEmail(user.getEmail());
        if(userByEmail != null){
            throw new RuntimeException("User Already Exists");
        }

            User newUser = new User();
            newUser.setUserName(user.getUserName());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            newUser.setRole(user.getRole());
            userRepositoy.save(newUser);

            RegisterResponseDTO response = new RegisterResponseDTO();
            response.setEmail(newUser.getEmail());
            response.setUserName(newUser.getUserName());
            response.setRole(newUser.getRole());

            return response;

    }

    public LoginResponseDTO login(User user){
        User newUser = userRepositoy.findByEmail(user.getEmail());
        if(newUser==null){
            throw new RuntimeException("User Not Found!");
        }
        if(!passwordEncoder.matches(user.getPassword(),newUser.getPassword())){
            throw new RuntimeException("Invalid Password");
        }

        String token = jwtUtil.generateToken(newUser.getEmail(),
        newUser.getRole());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setUserName(newUser.getUserName());
        response.setRole(newUser.getRole());
        response.setToken(token);
        response.setUserId(user.getId());
        return response;
    }
}
