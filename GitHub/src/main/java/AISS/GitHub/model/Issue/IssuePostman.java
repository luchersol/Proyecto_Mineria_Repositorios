
package AISS.GitHub.model.Issue;

import java.util.List;

import AISS.GitHub.model.User.User;
import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IssuePostman {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("number")
    private Integer ref_id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("body")
    private String description;
    @JsonProperty("state")
    private String state;
    @JsonProperty("created_at")
    private String created_at;
    @JsonProperty("updated_at")
    private String updated_at;
    @JsonProperty("closed_at")
    private String closed_at;
    @JsonProperty("labels")
    private List<Label> labels;
    @JsonProperty("reactions")
    private Reactions reactions;
    @JsonProperty("user")
    private User author;
    @JsonProperty("assignee")
    private User assignee;

    @JsonProperty("html_url")
    private String web_url;


    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("number")
    public Integer getRef_id() {
        return ref_id;
    }

    @JsonProperty("number")
    public void setRef_id(Integer ref_id) {
        this.ref_id = ref_id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("labels")
    public List<Label> getLabels() {
        return labels;
    }

    @JsonProperty("labels")
    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("created_at")
    public String getCreated_at() {
        return created_at;
    }

    @JsonProperty("created_at")
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @JsonProperty("updated_at")
    public String getUpdated_at() {
        return updated_at;
    }

    @JsonProperty("updated_at")
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @JsonProperty("closed_at")
    public String getClosed_at() {
        return closed_at;
    }

    @JsonProperty("closed_at")
    public void setClosed_at(String closed_at) {
        this.closed_at = closed_at;
    }

    @JsonProperty("body")
    public String getDescription() {
        return description;
    }

    @JsonProperty("body")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("reactions")
    public Reactions getReactions() { return reactions; }

    @JsonProperty("reactions")
    public void setReactions(Reactions reactions) {
        this.reactions = reactions;
    }

    @JsonProperty("user")
    public User getAuthor() {
        return author;
    }

    @JsonProperty("user")
    public void setAuthor(User author) {
        this.author = author;
    }

    @JsonProperty("assignee")
    public User getAssignee() {
        return assignee;
    }

    @JsonProperty("assignee")
    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", ref_id=" + ref_id +
                ", title='" + title + '\'' +
   //             ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                ", createdAt='" + created_at + '\'' +
                ", updatedAt='" + updated_at + '\'' +
                ", closedAt=" + closed_at +
                ", labels=" + labels.stream().map(label -> label.getName()).toList() +
                ", upvotes=" + reactions.getUpvoted() +
                ", downvotes=" + reactions.getDownvoted() +
                ", author=" + author +
                ", assignee=" + assignee +
                '}';
    }
}
