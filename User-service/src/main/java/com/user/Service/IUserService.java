package com.user.Service;
import com.user.DTO.UserDto;
import com.user.entities.Users;
import org.apache.catalina.User;

public interface IUserService {
    Users registerNewUserAccount(UserDto userDto);
}
