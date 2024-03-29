package com.example.userservice.service.impl;

import com.example.userservice.domain.Confirmation;
import com.example.userservice.domain.User;
import com.example.userservice.repository.ConfirmationRepo;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.EmailService;
import com.example.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationRepo confirmationRepo;
    private final EmailService emailService;

    @Override
    public User saveUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        user.setEnabled(false);  // means disable
        userRepository.save(user);

//      Creating confirmation  where user token exists

        Confirmation confirmation = new Confirmation(user);
        confirmationRepo.save(confirmation);

        /* TODO SEND EMAIL TO USER WITH TOKEN */
        emailService.sendSimpleMailMessage(user.getName(), user.getEmail(), confirmation.getToken());   //calling email service that sends simple email
        emailService.sendMimeMessageWithAttachments(user.getName(), user.getEmail(), confirmation.getToken());
        emailService.sendMimeMessageWithEmbeddedImages(user.getName(), user.getEmail(), confirmation.getToken());
        emailService.sendHtmlEmail(user.getName(), user.getEmail(), confirmation.getToken());

        return user;
    }

    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepo.findByToken(token);    // So we got the token. We send that token to the db to fetch an entire confirmation by that token and
                                                                            // then we find the user thai is associated with this confirmation
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);       // means enable user
        //confirmationRepo.delete(confirmation);   // delete particular confirmation
        return Boolean.TRUE;
    }

}
