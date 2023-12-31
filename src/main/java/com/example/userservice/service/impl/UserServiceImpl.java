package com.example.userservice.service.impl;

import com.example.userservice.domain.Confirmation;
import com.example.userservice.domain.User;
import com.example.userservice.repository.ConfirmationRepo;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationRepo confirmationRepo;

    @Override
    public User saveUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        user.setEnabled(false);
        userRepository.save(user);

        Confirmation confirmation = new Confirmation(user);
        confirmationRepo.save(confirmation);

        /* TODO SEND EMAIL TO USER WITH TOKEN */
        return user;
    }

    @Override
    public Boolean verifyToken(String token) {
        return null;
    }
}
