package aiss.gitminer.controller;



import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.CommentRepository;
import aiss.gitminer.repository.IssueRepository;
import aiss.gitminer.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name = "Comment", description = "Comment management API")
@RestController
@RequestMapping("/gitminer")
public class CommentController {

@Autowired
CommentRepository commentRepository;

@Autowired
IssueRepository issueRepository;

    @Operation(summary = "Retrieve all comments", description = "Get all comments. You can filter by author id and order the results", tags = { "comments", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Project.class), mediaType = "application/json") })
    })
    @GetMapping("/comments")
    public  List<Comment> getAllComments( @Parameter(description = "Id of the author we want to filter") @RequestParam(required = false) String authorId,
                                          @Parameter(description = "sort parameter") @RequestParam(required = false) String order,
                                          @Parameter(description = "number of page") @RequestParam(defaultValue = "0") Integer page,
                                          @Parameter(description = "size of page") @RequestParam(defaultValue = "10") Integer size){
        Pageable paging;
        if(order != null){
            if(order.startsWith("-")) {
                paging = PageRequest.of(page, size, Sort.by(order.substring(1)).descending());
            }else{
                paging = PageRequest.of(page, size, Sort.by(order).ascending());
            }
        } else {
            paging = PageRequest.of(page, size);
        }

        Page<Comment> pageComment;
        if(authorId != null){
            pageComment = commentRepository.findByAuthorId(authorId, paging);
         } else{
            pageComment = commentRepository.findAll(paging);
         }
        return pageComment.getContent();
    }


    // GET http://localhost:8080/gitminer/comments/{id}
    @Operation(summary = "Retrieve a comment given its id", description = "Get a comment once its id is given", tags = { "comments", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment found", content = { @Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Comment not found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("comments/{id}")
    public Comment getCommentById(
            @Parameter(description = "id of the comment that we want to see") @PathVariable(value = "id") String id) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
    if(!comment.isPresent()){
        throw new CommentNotFoundException();
    }
        return comment.get();
    }

    // POST http://localhost:8080/gitminer/issues/{id}/comments
    @Operation(summary = "Post a comment in an issue given its id", description = "Post the comment once the issue id is given", tags = { "comments", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comment created", content = { @Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Issue not found", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("/issues/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(
            @Parameter(description = "the comment we want to post") @RequestBody @Valid Comment comment,
            @Parameter(description = "id of the issue in which we want to insert a comment") @PathVariable("id") String id) throws IssueNotFoundException {
        Optional<Issue> issue = issueRepository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        issue.get().getComments().add(comment);
        issueRepository.save(issue.get());
        return comment;
    }

    // PUT http://localhost:8080/gitminer/comments/{id}
    @Operation(summary = "Update a comment once its given its id", description = "Updtate the comment which id is given", tags = { "comments", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Comment updated", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description="Issue not found", content = { @Content(schema = @Schema()) })
    })
    @PutMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateComment(
            @Parameter(description = "new values of the comment") @RequestBody @Valid Comment updatedComment,
            @Parameter(description = "id of the comment that we want to updated") @PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
        if(!comment.isPresent()){
            throw new CommentNotFoundException();
        }
        comment.get().setUpdatedAt(updatedComment.getUpdatedAt());
        comment.get().setBody(updatedComment.getBody());
        comment.get().setAuthor(updatedComment.getAuthor());
        comment.get().setCreatedAt(updatedComment.getCreatedAt());
        commentRepository.save(comment.get());
    }

    // DELETE http://localhost:8080/gitminer/comments/{id}
    @Operation(summary = "Delete a comment once its given its id", description = "Delete the comment which id is given", tags = { "comments", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Comment deleted", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description="Issue not found", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@Parameter(description = "id of the comment that we want to delete") @PathVariable String id) throws CommentNotFoundException {
        if(!commentRepository.existsById(id)){
            throw  new CommentNotFoundException();
        }
        commentRepository.deleteById(id);
    }


}
