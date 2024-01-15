package org.example.services.impl;

import org.example.CreateUserDto;
import org.example.Message;
import org.example.dao.UserDao;
import org.example.dao.impl.collection.UserMapDaoImpl;
import org.example.entyty.User;
import org.example.services.UserService;

public class UserServiceImpl extends AbstractService implements UserService {

    @Override
    public Message createUser(Message message) {
        CreateUserDto createUserDto = message.getCreateUserDto();
        //TODO add validation
        User user = new User();
        user.setName(createUserDto.getName());
        user.setGold(createUserDto.getGold());
        UserDao userDao = new UserMapDaoImpl();
        userDao.store(user);
        logger.info(String.format("User with name: \"%s\" was created", user.getName()));
        return prepareMessageConfirm(message);
    }
}
