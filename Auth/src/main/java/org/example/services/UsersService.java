package org.example.services;

import org.example.entities.Users;
import org.example.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users registerNewUser(String username, String password, String role){
        if(usersRepository.findByUsername(username).isPresent()){
            throw new RuntimeException("This user is already exists.");
        }

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        return usersRepository.save(user);
    }

    public Users findUserByUsername(String username){
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("No user with this username."));
    }
}
