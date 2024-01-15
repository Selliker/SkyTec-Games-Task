package org.example.services.impl;

import org.example.*;
import org.example.dao.ClanDao;
import org.example.dao.UserDao;
import org.example.dao.impl.collection.ClanMapDaoImpl;
import org.example.dao.impl.collection.UserMapDaoImpl;
import org.example.entyty.Clan;
import org.example.entyty.User;
import org.example.services.ClanService;

public class ClanServiceImpl extends AbstractService implements ClanService {

    @Override
    public Message createClan(Message message) {
        CreateClanDto createClanDto = message.getCreateClanDto();
        //TODO add validation
        Clan clan = new Clan();
        clan.setName(createClanDto.getName());
        clan.setGold(createClanDto.getGold());
        ClanDao clanDao = new ClanMapDaoImpl();
        clanDao.store(clan);
        logger.info(String.format("Clan with name: \"%s\" was created", clan.getName()));
        return prepareMessageConfirm(message);
    }

    @Override
    public Message addMoneyToClan(Message message) {
        ClanDao clanDao = new ClanMapDaoImpl();
        UserDao userDao = new UserMapDaoImpl();
        AddMoneyDto addMoneyDto = message.getAddMoneyDto();
        //TODO Add transaction
        userDao.takeGold(addMoneyDto.getUserId(), addMoneyDto.getGold());
        clanDao.addGold(addMoneyDto.getClanId(), addMoneyDto.getGold());
        logger.info(String.format("User with id: \"%d\" add money to clan with id: \"%d\". Gold: \"%d\"",
                addMoneyDto.getUserId(),
                addMoneyDto.getClanId(),
                addMoneyDto.getGold()));
        return prepareMessageConfirm(message);
    }

    @Override
    public Message takeMoneyFromClan(Message message) {
        ClanDao clanDao = new ClanMapDaoImpl();
        UserDao userDao = new UserMapDaoImpl();
        TakeMoneyDto takeMoneyDto = message.getTakeMoneyDto();
        //TODO Add transaction
        clanDao.takeGold(takeMoneyDto.getClanId(), takeMoneyDto.getGold());
        userDao.addGold(takeMoneyDto.getUserId(), takeMoneyDto.getGold());
        logger.info(String.format("User with id: \"%d\" take money from clan with id: \"%d\". Gold: \"%d\"",
                takeMoneyDto.getUserId(),
                takeMoneyDto.getClanId(),
                takeMoneyDto.getGold()));
        return prepareMessageConfirm(message);
    }

}
