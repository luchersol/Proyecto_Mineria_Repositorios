package GitLabMiner.restGitLab.test;

import GitLabMiner.restGitLab.models.Issue;
import GitLabMiner.restGitLab.service.IssueService;
import GitLabMiner.restGitLab.service.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class IssuesTest {
    @Autowired
    IssueService service;
    @Test
    @DisplayName("ListIssues")
    public void issuesTest(){

        List<Issue> ls = service.getIssues("4207231",5000,5);
        for(Issue is : ls){

            System.out.println(is.getRefId());
        }
        //System.out.println(service.getIssueById("4207231","2283").getLabels());

    }

}
