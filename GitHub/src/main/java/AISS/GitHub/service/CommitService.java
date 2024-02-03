package AISS.GitHub.service;

import AISS.GitHub.model.Commit.CommitPostman;
import AISS.GitHub.model.Commit.Commit;
import AISS.GitHub.util.Util;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommitService {
    @Autowired
    RestTemplate restTemplate;

    @Value("${token}")
    private String token;

    public List<Commit> getCommits(String owner, String repo, Integer sinceCommits, Integer maxPages){
        LocalDateTime date = LocalDateTime.now().minusDays(sinceCommits);
        String uri="https://api.github.com/repos/"+owner+"/"+repo+"/commits?since="+date;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        List<Commit> commits = new ArrayList<>();

        for (int i = 0; i < maxPages; i++) {
            HttpEntity<CommitPostman[]> request = new HttpEntity<>(null, headers);
            ResponseEntity<CommitPostman[]> response = restTemplate.exchange(uri, HttpMethod.GET, request, CommitPostman[].class);

            Arrays.stream(response.getBody()).forEach(commitPostman -> {
                    Commit commit = new Commit(
                            commitPostman.getId(),
                            commitPostman.getCommit().getTitle(),
                            commitPostman.getCommit().getMessage(),
                            commitPostman.getCommit().getAuthor().getName(),
                            commitPostman.getCommit().getAuthor().getEmail(),
                            commitPostman.getCommit().getAuthor().getDate(),
                            commitPostman.getCommit().getCommitter().getName(),
                            commitPostman.getCommit().getCommitter().getEmail(),
                            commitPostman.getCommit().getCommitter().getDate(),
                            commitPostman.getUrl());
                    commits.add(commit);
                    });

            uri = Util.getNextPageUrl(response.getHeaders());
            if(uri == null) break;
        }

        return commits;
    }



}
