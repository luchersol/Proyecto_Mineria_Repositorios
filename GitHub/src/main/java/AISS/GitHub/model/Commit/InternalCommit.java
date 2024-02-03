
package AISS.GitHub.model.Commit;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InternalCommit {

    @JsonProperty("author")
    private AuthorCommitter author;
    @JsonProperty("committer")
    private AuthorCommitter committer;
    @JsonProperty("message")
    private String message;

    private String title;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("author")
    public AuthorCommitter getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(AuthorCommitter author) {
        this.author = author;
    }

    @JsonProperty("committer")
    public AuthorCommitter getCommitter() {
        return committer;
    }

    @JsonProperty("committer")
    public void setCommitter(AuthorCommitter committer) {
        this.committer = committer;
    }

    public String getTitle() {
        String title = "";
        if(message.indexOf("\n")>0)
            title = message.substring(0, message.indexOf("\n"));
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
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
        StringBuilder sb = new StringBuilder();
        sb.append(InternalCommit.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("author");
        sb.append('=');
        sb.append(((this.author == null)?"<null>":this.author));
        sb.append(',');
        sb.append("committer");
        sb.append('=');
        sb.append(((this.committer == null)?"<null>":this.committer));
        sb.append(',');
        sb.append("message");
        sb.append('=');
        sb.append(((this.message == null)?"<null>":this.message));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
