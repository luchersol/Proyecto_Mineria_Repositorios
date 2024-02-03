package AISS.GitHub.service;

import AISS.GitHub.model.Comment.Comment;
import AISS.GitHub.model.Issue.Issue;
import AISS.GitHub.model.Issue.IssuePostman;
import AISS.GitHub.model.User.User;
import AISS.GitHub.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class IssueService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Value("${token}")
    private String token;

    public List<Issue> getIssues(String owner, String repo, Integer sinceIssues, Integer maxPages){
        LocalDateTime date = LocalDateTime.now().minusDays(sinceIssues);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        List<Issue> issues = new ArrayList<>();
        String uri="https://api.github.com/repos/"+ owner + "/"+ repo +"/issues?since="+date;

        for (int i = 0; i < maxPages; i++) {
            HttpEntity<IssuePostman[]> request = new HttpEntity<>(null, headers);
            ResponseEntity<IssuePostman[]> response = restTemplate.exchange(uri, HttpMethod.GET, request, IssuePostman[].class);

            Arrays.stream(response.getBody()).forEach(issuePostman -> {
                        User author = userService.getUserByUsername(issuePostman.getAuthor().getUsername());
                        User assignee = issuePostman.getAssignee() == null ? null : userService.getUserByUsername(issuePostman.getAssignee().getUsername());
                        List<Comment> comments = commentService.getCommentsById(owner, repo, issuePostman.getRef_id().toString(), maxPages);

                        Issue issue = new Issue(
                                issuePostman.getId().toString(),
                                issuePostman.getRef_id().toString(),
                                issuePostman.getTitle(),
                                issuePostman.getDescription(),
                                issuePostman.getState(),
                                issuePostman.getCreated_at(),
                                issuePostman.getUpdated_at(),
                                issuePostman.getClosed_at(),
                                issuePostman.getLabels().stream().map(label -> label.getName()).toList(),
                                issuePostman.getReactions().getUpvoted(),
                                issuePostman.getReactions().getDownvoted(),
                                author,
                                assignee,
                                comments,
                                issuePostman.getWeb_url()
                        );
                        issues.add(issue);
                    });

            uri = Util.getNextPageUrl(response.getHeaders());
            if (uri == null) break;
        }

        return issues;
    }

}
