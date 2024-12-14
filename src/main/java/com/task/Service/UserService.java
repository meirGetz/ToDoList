package com.task.Service;

import com.task.DTO.UserDto;
import com.task.entities.Users;
import com.task.exceptions.UserAlreadyExistException;
import com.task.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private PasswordEncoder passwordEncoder;  // הוספת BCryptPasswordEncoder

    @Autowired
    private UserRepository repository;

    @Override
    public Users registerNewUserAccount(UserDto userDto) throws UserAlreadyExistException {
        if (emailExists(userDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + userDto.getEmail());
        }

        Users user = new Users();
        user.setUsername(userDto.getFirstName() + " " + userDto.getLastName());
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encryptedPassword);
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setRole("USER");

        return repository.save(user);
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email) != null;
    }
}
