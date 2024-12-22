package com.task.Service;
import com.task.DTO.UserDto;
import com.task.entities.Users;
import org.apache.catalina.User;

public interface IUserService {
    Users registerNewUserAccount(UserDto userDto);
}
