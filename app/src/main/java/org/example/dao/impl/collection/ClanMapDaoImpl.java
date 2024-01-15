package org.example.dao.impl.collection;

import org.example.dao.ClanDao;
import org.example.entyty.Clan;

import java.util.concurrent.ConcurrentHashMap;

public class ClanMapDaoImpl implements ClanDao {

    private static volatile Long max_id = 1L;
    private static ConcurrentHashMap<Long, Clan> clanConcurrentHashMap = new ConcurrentHashMap<>();

    @Override
    public void store(Clan clan) {
            synchronized (max_id) {
                clan.setId(max_id++);
            }
        clanConcurrentHashMap.put(clan.getId(), clan);
    }

    @Override
    public Clan get(long clanId) {
        return clanConcurrentHashMap.get(clanId);
    }

    @Override
    public void addGold(long clanId, int gold) {
        Clan clan = clanConcurrentHashMap.get(clanId);
        synchronized (clan) {
            int current_cold = clan.getGold() + gold;
            clan.setGold(current_cold);
        }
    }

    @Override
    public void takeGold(long clanId, int gold) {
        Clan clan = clanConcurrentHashMap.get(clanId);
        synchronized (clan) {
            int current_cold = clan.getGold() - gold;
            if(current_cold < 0){
                throw new ArithmeticException("Not enough gold");
            }
            clan.setGold(current_cold);
        }
    }
}
