package com.todolist.Service;

import com.todolist.commonmodule.DTO.UserDto;
import com.todolist.commonmodule.entities.Users;
import com.todolist.commonmodule.repositories.UserRepository;
import com.todolist.commonmodule.security.exceptions.UserAlreadyExistException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

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
