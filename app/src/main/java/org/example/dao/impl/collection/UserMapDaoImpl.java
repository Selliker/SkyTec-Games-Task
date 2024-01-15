package org.example.dao.impl.collection;

import org.example.dao.UserDao;
import org.example.entyty.Clan;
import org.example.entyty.User;

import java.util.concurrent.ConcurrentHashMap;

public class UserMapDaoImpl implements UserDao {

    private static volatile Long max_id = 1L;
    private static ConcurrentHashMap<Long, User> userConcurrentHashMap = new ConcurrentHashMap<>();

    @Override
    public void store(User user) {
            synchronized (max_id) {
                user.setId(max_id++);
            }
        userConcurrentHashMap.put(user.getId(), user);
    }

    @Override
    public User get(long userId) {
        return userConcurrentHashMap.get(userId);
    }

    @Override
    public void addGold(long userId, int gold) {
        User user = userConcurrentHashMap.get(userId);
        synchronized (user) {
            int current_cold = user.getGold() + gold;
            user.setGold(current_cold);
        }
    }

    @Override
    public void takeGold(long userId, int gold) {
        User user = userConcurrentHashMap.get(userId);
        synchronized (user) {
            int current_cold = user.getGold() - gold;
            if(current_cold < 0){
                throw new ArithmeticException("Not enough gold");
            }
            user.setGold(current_cold);
        }
    }
}
