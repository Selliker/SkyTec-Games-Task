package org.example.services;

import org.example.CreateUserDto;
import org.example.Message;

public interface UserService {
    Message createUser(Message message);
}
