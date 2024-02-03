package GitLabMiner.restGitLab.test;

import GitLabMiner.restGitLab.models.Project;
import GitLabMiner.restGitLab.service.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
public class ProjectTests {

    @Autowired
    ProjectService service;

    @Test
    @DisplayName("ListProjects")
   public void projectTest(){
        List<Project> ls = service.getProjects(2);
        for(Project pr : ls ){
                System.out.println(pr.getId());
            }
            System.out.println(service.getProjectById("13303426"));

    }


}
