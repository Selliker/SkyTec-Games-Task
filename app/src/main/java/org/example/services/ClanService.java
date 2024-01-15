package org.example.services;

import org.example.AddMoneyDto;
import org.example.CreateClanDto;
import org.example.Message;
import org.example.TakeMoneyDto;

public interface ClanService {


    Message createClan(Message message);
    Message addMoneyToClan(Message message);
    Message takeMoneyFromClan(Message message);
}
