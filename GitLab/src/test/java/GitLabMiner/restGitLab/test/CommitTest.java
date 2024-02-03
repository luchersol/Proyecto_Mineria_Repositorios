package GitLabMiner.restGitLab.test;

import GitLabMiner.restGitLab.models.Commit;
import GitLabMiner.restGitLab.service.CommitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CommitTest {

    @Autowired
    CommitService service;
    @Test
    @DisplayName("ListCommit")
    void commitTest() {
        List<Commit> ls = service.getCommitsByProject("13303426",1000,2);
        for(Commit c : ls){
            System.out.println(c.getId());
        }
    }




}
