package edu.scu.my_shop.service;

import edu.scu.my_shop.dao.MessageMapper;
import edu.scu.my_shop.dao.UserMapper;
import edu.scu.my_shop.entity.Message;
import edu.scu.my_shop.entity.MessageExample;
import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.exception.MessageException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static edu.scu.my_shop.exception.MessageException.*;

/**
 * Created by Vicent_Chen on 2018/3/20.
 */
@Service
public class MessageService {
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * Check user.
     * userID cannot be null and the correspond user need to be found in DB.
     * @param userID
     */
    private void checkUser(String userID) {
        if (userID == null || userID.equals("")) {
            throw new MessageException(MessageException.USER_EMPTY_MESSAGE, USER_EMPTY);
        }
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.selectByPrimaryKey(userID);
        if (user == null) {
            throw new MessageException(USER_EMPTY_MESSAGE, USER_EMPTY);
        }
        sqlSession.close();
    }

    /**
     * Check message.
     * Message cannot be null.
     * Message must have legal sender and receiver
     * @param message
     */
    private void checkMessage(Message message) {
        if (message == null) {
            throw new MessageException(NULL_MESSAGE_MESSAGE, NULL_MESSAGE);
        }
        checkUser(message.getSenderId());
        checkUser(message.getRecvId());
    }

    /**
     * Insert message into DB.
     * Currently only support point-to-point message.
     * Auto set 'is_read' column as false
     * @param message
     */
    @Transactional
    public void insertMessage(Message message) {
        checkMessage(message);

        // auto generate id
        String messageID = UUID.randomUUID().toString();
        message.setMessageId(messageID);

        // insert into DB
        SqlSession sqlSession = sqlSessionFactory.openSession();
        MessageMapper messageMapper = sqlSession.getMapper(MessageMapper.class);
        messageMapper.insertSelective(message);
        sqlSession.close();
    }

    /**
     * Delete message from DB.
     * Auto ignore message that not exist.
     * @param messageID
     */
    @Transactional
    public void deleteMessage(String messageID) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        MessageMapper messageMapper = sqlSession.getMapper(MessageMapper.class);
        messageMapper.deleteByPrimaryKey(messageID);
        sqlSession.close();
    }

    /**
     * Get all message that hasn't been read.
     * Set all unread messages as read.
     * @param userID
     * @return
     */
    @Transactional
    public List<Message> getUnreadMessage(String userID) {
        MessageExample messageExample = new MessageExample();
        messageExample.createCriteria().andRecvIdEqualTo(userID).andIsReadEqualTo(false);
        Message message = new Message();
        message.setIsRead(true);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        MessageMapper messageMapper = sqlSession.getMapper(MessageMapper.class);

        // get message list
        List<Message> messageList = messageMapper.selectByExample(messageExample);

        // update message list
        messageMapper.updateByExampleSelective(message, messageExample);

        sqlSession.close();

        return messageList;
    }

    public List<Message> getAllMessage(String userID) {
        MessageExample messageExample = new MessageExample();
        messageExample.createCriteria().andRecvIdEqualTo(userID).andIsReadEqualTo(true);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        MessageMapper messageMapper = sqlSession.getMapper(MessageMapper.class);
        List<Message> readList = messageMapper.selectByExample(messageExample);
        sqlSession.close();

        List<Message> unreadList = getUnreadMessage(userID);

        List<Message> messageList = new ArrayList<>();
        messageList.addAll(unreadList);
        messageList.addAll(readList);

        return messageList;
    }

}
