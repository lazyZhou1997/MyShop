package edu.scu.my_shop.service;

import edu.scu.my_shop.dao.CommentMapper;
import edu.scu.my_shop.entity.Comment;
import edu.scu.my_shop.entity.CommentExample;
import edu.scu.my_shop.entity.Product;
import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.exception.CommentException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static edu.scu.my_shop.exception.CommentException.*;

/**
 * Created by Vicent_Chen on 2018/3/20.
 */
@Service
public class CommentService {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private ProductService productService;

    @Autowired
    private ChangeUserInfoService changeUserInfoService;

    /**
     * Insert a comment into database.
     * Comment ID will be generated.
     * Comment Date will be generated <b>if it's null</b>.
     * @param comment
     */
    @Transactional
    public void insertComment(Comment comment) {

        if (comment == null) {
            throw new CommentException(NULL_COMMENT_MESSAGE, NULL_COMMENT);
        }
        if (comment.getCommentContent() == null) {
            throw new CommentException(COMMENT_ERROR_MESSAGE, COMMENT_ERROR);
        }

        String productID = comment.getProductId();
        String userID = comment.getUserId();

        if (!productService.productExists(productID)) {
            throw new CommentException(PRODUCT_ERROR_MESSAGE, PRODUCT_ERROR);
        }
        if (!changeUserInfoService.userExists(userID)) {
            throw new CommentException(USER_ERROR_MESSAGE, USER_ERROR);
        }

        // auto genereate comment id
        String commentID = UUID.randomUUID().toString();
        comment.setCommentId(commentID);

        // auto add comment date
        if (comment.getCommentDate() == null) {
            comment.setCommentDate(new Date());
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        CommentMapper commentMapper = sqlSession.getMapper(CommentMapper.class);
        commentMapper.insert(comment);
        sqlSession.close();
    }

    /**
     * Update comment in database.
     * If userID is not null, it will be checked.
     * If productId is not null, it will be checked.
     * @param comment
     */
    @Transactional
    public void updateComment(Comment comment) {
        if (comment == null) {
            throw new CommentException(NULL_COMMENT_MESSAGE, NULL_COMMENT);
        }

        String productID = comment.getProductId();
        String userID = comment.getUserId();

        if (productID != null && !productService.productExists(productID)) {
            throw new CommentException(PRODUCT_ERROR_MESSAGE, PRODUCT_ERROR);
        }
        if (userID != null && !changeUserInfoService.userExists(userID)) {
            throw new CommentException(USER_ERROR_MESSAGE, USER_ERROR);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        CommentMapper commentMapper = sqlSession.getMapper(CommentMapper.class);
        commentMapper.updateByPrimaryKeySelective(comment);
        sqlSession.close();
    }

    /**
     * Delete comment from database.
     * Ignore not exist ID.
     * @param commentID
     */
    @Transactional
    public void deleteComment(String commentID) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        CommentMapper commentMapper = sqlSession.getMapper(CommentMapper.class);
        commentMapper.deleteByPrimaryKey(commentID);
        sqlSession.close();
    }

    /**
     * Search comments of a product.
     * It will not return null instead an empty list.
     * @param productID
     * @return
     */
    public List<Comment> getCommentByProduct(String productID) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andProductIdEqualTo(productID);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        CommentMapper commentMapper = sqlSession.getMapper(CommentMapper.class);
        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        sqlSession.close();

        return commentList;
    }

    /**
     * Search comments of a user.
     * It will not return null instead an empty list.
     * @param userID
     */
    public List<Comment> getCommentByUser(String userID) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andUserIdEqualTo(userID);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        CommentMapper commentMapper = sqlSession.getMapper(CommentMapper.class);
        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        sqlSession.close();

        return commentList;
    }

    /**
     * Select comments of specified date field.
     * If parameter begin or end is null, <b>it will be replaced as current Date</b>.
     * @param begin
     * @param end
     */
    public List<Comment> getCommentByDate(Date begin, Date end) {
        // replace null
        if (begin == null) {
            begin = new Date();
        }
        if (end == null) {
            end = new Date();
        }

        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andCommentDateBetween(begin, end);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        CommentMapper commentMapper = sqlSession.getMapper(CommentMapper.class);
        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        sqlSession.close();

        return commentList;
    }

    /**
     * Return the number of comments of a product.
     * @param productID
     * @return
     */
    public Long getCommentNumberByProduct(String productID) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andProductIdEqualTo(productID);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        CommentMapper commentMapper = sqlSession.getMapper(CommentMapper.class);
        Long count = commentMapper.countByExample(commentExample);
        sqlSession.close();

        return count;
    }

    /**
     * Chec if comment exists
     * @param commentID
     * @return
     */
    public boolean commentExists(String commentID) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        CommentMapper commentMapper = sqlSession.getMapper(CommentMapper.class);
        Comment comment = commentMapper.selectByPrimaryKey(commentID);
        sqlSession.close();
        return comment != null;
    }
}
