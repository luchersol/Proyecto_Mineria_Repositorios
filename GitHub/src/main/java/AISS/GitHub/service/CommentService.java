package AISS.GitHub.service;

import AISS.GitHub.model.Comment.Comment;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserService userService;

    @Value("${token}")
    private String token;

    public List<Comment> getCommentsById(String owner, String repo, String id, Integer maxPages){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        String uri = "https://api.github.com/repos/" + owner + "/" + repo + "/issues/" + id + "/comments";
        List<Comment> comments = new ArrayList<>();

        for (int i = 0; i < maxPages; i++) {
            HttpEntity<Comment[]> request = new HttpEntity<>(null, headers);
            ResponseEntity<Comment[]> response = restTemplate.exchange(uri, HttpMethod.GET, request, Comment[].class);

            Arrays.stream(response.getBody()).forEach(comment -> {
                User author = userService.getUserByUsername(comment.getAuthor().getUsername());
                comment.setAuthor(author);
                comments.add(comment);
            });

            uri = Util.getNextPageUrl(response.getHeaders());
            if(uri == null) break;
        }

        return comments;
    }

}
