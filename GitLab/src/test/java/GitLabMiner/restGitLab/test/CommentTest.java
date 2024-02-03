package GitLabMiner.restGitLab.test;

import GitLabMiner.restGitLab.models.Comment;
import GitLabMiner.restGitLab.models.Commit;
import GitLabMiner.restGitLab.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CommentTest {

    @Autowired
    CommentService service;
    @Test
    @DisplayName("ListComments")
    void commitTest() {
        List<Comment> cm = service.getComments("4207231", "2357",5);
        for(Comment c : cm){
            System.out.println(c);
        }
    }




}
