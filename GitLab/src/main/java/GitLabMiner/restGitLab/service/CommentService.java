package GitLabMiner.restGitLab.service;

import GitLabMiner.restGitLab.models.Comment;
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

    @Value("${token}")
    private String token;
    private String baseUri= "https://gitlab.com/api/v4/projects/";
    public List<Comment> getComments(String projectId ,String issueId,Integer pages){
        HttpHeaders headers = new HttpHeaders();
        List<Comment> res = new ArrayList<>();
        headers.setBearerAuth(token);
        HttpEntity<Comment[]> request = new HttpEntity<>(null, headers);
        for(int i = 1 ; i <= pages; i++){
            ResponseEntity<Comment[]> response = restTemplate.exchange(baseUri+projectId+"/issues/"+issueId+"/notes?page="+i,HttpMethod.GET,request,Comment[].class);
            if(!response.hasBody()) break;
            res.addAll(Arrays.stream(response.getBody()).toList());
        }
        return res;
    }
}
