package com.user.Service;
import com.DTO.UserDto;
import com.user.entities.Users;

public interface IUserService {
    Users registerNewUserAccount(UserDto userDto);
}
