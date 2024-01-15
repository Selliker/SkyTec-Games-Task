package org.example.dao;

import org.example.entyty.Clan;
import org.example.entyty.User;

public interface UserDao {

    void store(User user);

    User get(long clanId);
    void addGold(long userId, int gold);
    void takeGold(long userId, int gold);
}
