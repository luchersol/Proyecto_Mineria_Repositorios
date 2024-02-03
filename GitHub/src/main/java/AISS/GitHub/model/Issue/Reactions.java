
package AISS.GitHub.model.Issue;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reactions {

    @JsonProperty("+1")
    private Integer upvoted;
    @JsonProperty("-1")
    private Integer downvoted;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("upvoted")
    public Integer getUpvoted() {
        return upvoted;
    }

    @JsonProperty("upvoted")
    public void setUpvoted(Integer upvoted) {
        this.upvoted = upvoted;
    }

    @JsonProperty("downvoted")
    public Integer getDownvoted() {
        return downvoted;
    }

    @JsonProperty("downvoted")
    public void setDownvoted(Integer downvoted) {
        this.downvoted = downvoted;
    }


    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
