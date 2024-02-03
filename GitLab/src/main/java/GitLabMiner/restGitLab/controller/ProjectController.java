package GitLabMiner.restGitLab.controller;
import GitLabMiner.restGitLab.models.Comment;
import GitLabMiner.restGitLab.models.Commit;
import GitLabMiner.restGitLab.models.Issue;
import GitLabMiner.restGitLab.models.Project;
import GitLabMiner.restGitLab.service.CommentService;
import GitLabMiner.restGitLab.service.CommitService;
import GitLabMiner.restGitLab.service.IssueService;
import GitLabMiner.restGitLab.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@RestController
@RequestMapping("/gitlab/projects")
public class ProjectController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CommitService commitService;
    @Autowired
    IssueService issueService;

    @Autowired
    CommentService commentService;
    @Autowired
    ProjectService projectService;

    //GET http://localhost:8081/gitlabMiner/projects/:id


    @GetMapping("/{id}")
    public Project getProjectById( @PathVariable String id,
                                       @RequestParam(defaultValue = "20") Integer sinceIssues,
                                       @RequestParam(defaultValue = "2") Integer sinceCommits,
                                       @RequestParam(defaultValue = "2") Integer maxPages
                                    ) {
        Project project = projectService.getProjectById(id);
        List<Commit> commits = commitService.getCommitsByProject(id,sinceCommits,maxPages);
        List<Issue> issues = issueService.getIssues(id,sinceIssues,maxPages);
        for(int i = 0; i < issues.size(); i++){
         List<Comment> comments = commentService.getComments(project.id,issues.get(i).getRefId(),maxPages);
         issues.get(i).setComments(comments);
        }
        project.setCommits(commits);
        project.setIssues(issues);
        return project;
    }

    // POST "http://localhost:8081/gitminer/projects"
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@PathVariable(name = "id") String id,
                                 @RequestParam(defaultValue = "20") Integer sinceIssues,
                                 @RequestParam(defaultValue = "2") Integer sinceCommits,
                                 @RequestParam(defaultValue = "2") Integer maxPages){
    Project project = getProjectById(id,sinceIssues,sinceCommits,maxPages);
    String uri = "http://localhost:8080/gitminer/projects";
    restTemplate.postForEntity(uri, project, Project.class);
    return project;
    }

}
