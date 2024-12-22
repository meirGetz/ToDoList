package com.todolist.Service;

import com.todolist.commonmodule.DTO.UserDto;
import com.todolist.commonmodule.entities.Users;

public interface IUserService {
    Users registerNewUserAccount(UserDto userDto);
}
