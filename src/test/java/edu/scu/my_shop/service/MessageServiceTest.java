package edu.scu.my_shop.service;

import edu.scu.my_shop.entity.Message;
import edu.scu.my_shop.exception.MessageException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static edu.scu.my_shop.exception.MessageException.NULL_MESSAGE;
import static edu.scu.my_shop.exception.MessageException.USER_EMPTY;
import static org.junit.Assert.*;

/**
 * Created by Vicent_Chen on 2018/3/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MessageServiceTest {

    @Autowired
    MessageService messageService;

    private Message nullMessage;
    private Message nullSender;
    private Message nullReceive;
    private Message message;

    @Before
    public void init() {
        nullMessage = null;

        nullSender = new Message();
        nullSender.setSenderId(null); nullSender.setRecvId("for-message-test-recver");

        nullReceive = new Message();
        nullReceive.setSenderId("for-message-test-sender"); nullReceive.setRecvId(null);

        message = new Message();
        message.setSenderId("for-message-test-sender"); message.setRecvId("for-message-test-recver");
        message.setMessageContent("Inserting now");

    }

    @Test
    public void exceptionInsertMessage() {
        try {
            messageService.insertMessage(nullMessage);
        } catch (MessageException e) {
            assertEquals(NULL_MESSAGE, e.getCode().longValue());
        }
        try {
            messageService.insertMessage(nullSender);
        } catch (MessageException e) {
            assertEquals(USER_EMPTY, e.getCode().longValue());
        }
        try {
            messageService.insertMessage(nullReceive);
        } catch (MessageException e) {
            assertEquals(USER_EMPTY, e.getCode().longValue());
        }
    }

    @Test
    public void insertMessage() {
        messageService.insertMessage(message);
    }

    @Test
    public void deleteMessage() {
        messageService.deleteMessage(null);
        messageService.deleteMessage(nullSender.getMessageId());
        messageService.deleteMessage(message.getMessageId());
    }

    @Test
    public void getUnreadMessage() {
        List<Message> messageList = messageService.getUnreadMessage("for-message-test-recver");
        System.out.println(messageList.size());
        messageService.getUnreadMessage("no-one");
    }

    @Test
    public void getAllMessage() {
        List<Message> messageList = messageService.getAllMessage("for-message-test-recver");
        for (Message message : messageList) {
            System.out.println(message.getIsRead() + " : " + message.getMessageContent());
        }
        messageService.getUnreadMessage("no-one");
    }
}