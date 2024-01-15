package org.example.dao;

import org.example.entyty.Clan;

public interface ClanDao {
    void store(Clan clan);
    Clan get(long clanId);
    void addGold(long clanId, int gold);
    void takeGold(long clanId, int gold);
}
