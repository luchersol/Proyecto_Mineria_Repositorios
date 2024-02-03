package AISS.GitHub.service;

import AISS.GitHub.model.Comment.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService service;

    @Test
    @DisplayName("Get all comments by issue id")
    void getCommentsById() {
        List<Comment> comments = service.getCommentsById("spring-projects", "spring-framework", "30355", 2);
        System.out.println(comments);
    }
}