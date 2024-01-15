package org.example.service;

import org.example.AddMoneyDto;
import org.example.Message;
import org.example.TakeMoneyDto;
import org.example.dao.UserDao;
import org.example.dao.impl.collection.UserMapDaoImpl;
import org.example.entyty.Clan;
import org.example.dao.ClanDao;
import org.example.dao.impl.collection.ClanMapDaoImpl;
import org.example.entyty.User;
import org.example.services.ClanService;
import org.example.services.impl.ClanServiceImpl;
import org.testng.annotations.Test;

import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class ClanServiceTest {

    @Test
    public void takeMoneyTest(){
        //Create clan
        ClanDao clanDao = new ClanMapDaoImpl();
        Clan clan = new Clan();
        clan.setName("NewClan" + new Random().nextLong());
        clan.setGold(3000);
        clanDao.store(clan);
        //Create user
        UserDao userDao = new UserMapDaoImpl();
        User user = new User();
        user.setName("GenshinPayer" + new Random().nextLong());
        user.setGold(100);
        userDao.store(user);

        //Test method
        ClanService clanService = new ClanServiceImpl();
        Message responseMessage = clanService.takeMoneyFromClan(Message.newBuilder()
                .setOpCode(Message.OpCode.TAKE_MONEY_FROM_CLAN)
                .mergeTakeMoneyDto(
                        TakeMoneyDto.newBuilder()
                                .setClanId(clan.getId())
                                .setUserId(user.getId())
                                .setGold(500)
                                .build())
                .build());
        assertEquals(responseMessage.getOpCode(), Message.OpCode.MESSAGE_CONFIRM);
        assertEquals(user.getGold(), 600);
        assertEquals(clan.getGold(), 2500);

        assertThrows(ArithmeticException.class, ()->{
            clanService.takeMoneyFromClan(Message.newBuilder()
                .setOpCode(Message.OpCode.TAKE_MONEY_FROM_CLAN)
                .mergeTakeMoneyDto(
                        TakeMoneyDto.newBuilder()
                                .setClanId(clan.getId())
                                .setUserId(user.getId())
                                .setGold(2600)
                                .build())
                .build());});
        assertEquals(user.getGold(), 600);
        assertEquals(clan.getGold(), 2500);
    }

    @Test
    public void addMoneyTest(){
        //Create clan
        ClanDao clanDao = new ClanMapDaoImpl();
        Clan clan = new Clan();
        clan.setName("NewClan" + new Random().nextLong());
        clan.setGold(1000);
        clanDao.store(clan);
        //Create user
        UserDao userDao = new UserMapDaoImpl();
        User user = new User();
        user.setName("GenshinPayer" + new Random().nextLong());
        user.setGold(1000);
        userDao.store(user);

        //Test method
        ClanService clanService = new ClanServiceImpl();
        Message responseMessage = clanService.addMoneyToClan(Message.newBuilder()
                .setOpCode(Message.OpCode.ADD_MONEY_TO_CLAN)
                .mergeAddMoneyDto(
                        AddMoneyDto.newBuilder()
                                .setClanId(clan.getId())
                                .setUserId(user.getId())
                                .setGold(500)
                                .build())
                .build());
        assertEquals(responseMessage.getOpCode(), Message.OpCode.MESSAGE_CONFIRM);
        assertEquals(user.getGold(), 500);
        assertEquals(clan.getGold(), 1500);

        assertThrows(ArithmeticException.class, ()->{
            clanService.addMoneyToClan(Message.newBuilder()
                    .setOpCode(Message.OpCode.ADD_MONEY_TO_CLAN)
                    .mergeAddMoneyDto(
                            AddMoneyDto.newBuilder()
                                    .setClanId(clan.getId())
                                    .setUserId(user.getId())
                                    .setGold(1000)
                                    .build())
                    .build());});
        assertEquals(user.getGold(), 500);
        assertEquals(clan.getGold(), 1500);
    }
}
