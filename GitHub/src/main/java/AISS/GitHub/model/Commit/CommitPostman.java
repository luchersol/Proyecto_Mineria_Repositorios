
package AISS.GitHub.model.Commit;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommitPostman {

    @JsonProperty("sha")
    private String id;

    @JsonProperty("url")
    private String url;

    @JsonProperty("commit")
    private InternalCommit commit;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("sha")
    public String getId() { return id; }

    @JsonProperty("sha")
    public void setId(String id) { this.id = id; }

    @JsonProperty("url")
    public String getUrl() { return url; }

    @JsonProperty("url")
    public void setUrl(String url) { this.url = url;}

    @JsonProperty("commit")
    public InternalCommit getCommit() {
        return commit;
    }

    @JsonProperty("commit")
    public void setCommit(InternalCommit commit) {
        this.commit = commit;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id=" + id +
                ", title=" + commit.getTitle() +
                ", message=" + commit.getMessage() +
                ", author_name=" + commit.getAuthor().getName() +
                ", author_email=" + commit.getAuthor().getEmail() +
                ", authored_date=" + commit.getAuthor().getDate() +
                ", commiter_name=" + commit.getCommitter().getName() +
                ", committer_email=" + commit.getCommitter().getEmail() +
                ", committed_date=" + commit.getCommitter().getDate() +
                ", web_url=" + url +
                '}';
    }
}
