package edu.scu.my_shop.service;

import edu.scu.my_shop.entity.Comment;
import edu.scu.my_shop.exception.CommentException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static edu.scu.my_shop.exception.CommentException.*;
import static org.junit.Assert.*;

/**
 * Created by Vicent_Chen on 2018/3/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    CommentService commentService;

    private Comment nullComment;
    private Comment nullID;
    private Comment nullProduct;
    private Comment nullUser;
    private Comment nullContent;
    private Comment nullDate;

    private Comment toInsert;
    private Comment toUpdate;
    private Comment toDelete;

    private Comment wrongProduct;
    private Comment wrongUser;

    @Before
    public void init() {
        nullComment = null;

        nullID = new Comment(); nullID.setCommentContent("null ID");
        nullID.setProductId("for-comment-test"); nullID.setUserId("for-comment-test");
        nullID.setCommentDate(new Date()); nullID.setCommentId(null);

        nullProduct = new Comment(); nullProduct.setCommentContent("null Product");
        nullProduct.setProductId(null); nullProduct.setUserId("for-comment-test");
        nullProduct.setCommentDate(new Date()); nullProduct.setCommentId("null Product");

        nullUser = new Comment(); nullUser.setCommentContent("nullUser");
        nullUser.setProductId("for-comment-test"); nullUser.setUserId(null);
        nullUser.setCommentDate(new Date()); nullUser.setCommentId("nullUser");

        nullContent = new Comment(); nullContent.setCommentContent(null);
        nullContent.setProductId("for-comment-test"); nullContent.setUserId("for-comment-test");
        nullContent.setCommentDate(new Date()); nullContent.setCommentId("nullContent");

        nullDate = new Comment(); nullDate.setCommentContent("nullDate");
        nullDate.setProductId("for-comment-test"); nullDate.setUserId("for-comment-test");
        nullDate.setCommentDate(null); nullDate.setCommentId("nullDate");

        toInsert = new Comment(); toInsert.setCommentContent("toInsert");
        toInsert.setCommentDate(new Date(2000, 12, 20));
        toInsert.setProductId("for-comment-test"); toInsert.setUserId("for-comment-test");

        toUpdate = new Comment(); toUpdate.setCommentContent("Updateddddddddd");
        toUpdate.setCommentId("toUpdate");

        toDelete = new Comment();
        toDelete.setCommentId("toDelete");

        wrongProduct = new Comment();
        wrongProduct.setCommentId("wrongProduct"); wrongProduct.setProductId("wrongProduct");

        wrongUser = new Comment();
        wrongUser.setCommentId("wrongUser"); wrongUser.setUserId("wrongUser");
    }

    @Test
    public void exceptionInsertComment() {
        try {
            commentService.insertComment(nullComment);
        } catch (CommentException e) {
            assertEquals(NULL_COMMENT, e.getCode().longValue());
        }
        try {
            commentService.insertComment(nullProduct);
        } catch (CommentException e) {
            assertEquals(PRODUCT_ERROR, e.getCode().longValue());
        }
        try {
            commentService.insertComment(nullUser);
        } catch (CommentException e) {
            assertEquals(USER_ERROR, e.getCode().longValue());
        }
        try {
            commentService.insertComment(nullContent);
        } catch (CommentException e) {
            assertEquals(COMMENT_ERROR, e.getCode().longValue());
        }
    }

    @Test
    public void insertComment() {
        commentService.insertComment(nullID);
        commentService.insertComment(nullDate);
        commentService.insertComment(toInsert);
    }

    @Test
    public void exceptionUpdateComment() {
        try {
            commentService.updateComment(nullComment);
        } catch (CommentException e) {
            assertEquals(NULL_COMMENT, e.getCode().longValue());
        }
        try {
            commentService.updateComment(wrongProduct);
        } catch (CommentException e) {
            assertEquals(PRODUCT_ERROR, e.getCode().longValue());
        }
        try {
            commentService.updateComment(wrongUser);
        } catch (CommentException e) {
            assertEquals(USER_ERROR, e.getCode().longValue());
        }
    }

    @Test
    public void updateComment() {
        commentService.updateComment(nullID);
        commentService.updateComment(toUpdate);
    }

    @Test
    public void deleteComment() {
        commentService.deleteComment("null");
        commentService.deleteComment("wrong");
        commentService.deleteComment(toDelete.getCommentId());
    }

    @Test
    public void getCommentByProduct() {
        System.out.println(commentService.getCommentByProduct("for-comment-test").size());
    }

    @Test
    public void getCommentByUser() {
        System.out.println(commentService.getCommentByUser("for-comment-test").size());
    }

    @Test
    public void getCommentByDate() {
        Date begin = new Date(116, 1, 1);
        Date end = new Date();
        System.out.println(commentService.getCommentByDate(begin, end).size());
        System.out.println(commentService.getCommentByDate(end, end).size());
        System.out.println(commentService.getCommentByDate(null, end).size());
        System.out.println(commentService.getCommentByDate(begin, null).size());
        System.out.println(commentService.getCommentByDate(null, null).size());
    }

    @Test
    public void getCommentNumberByProduct() {
        System.out.println(commentService.getCommentNumberByProduct("for-comment-test"));
    }

    @Test
    public void commentExists() {
        assertEquals(true, commentService.commentExists("toUpdate"));
        assertEquals(false, commentService.commentExists("notexists"));
        assertEquals(false, commentService.commentExists(null));
    }
}