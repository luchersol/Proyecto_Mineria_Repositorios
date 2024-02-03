package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.Project;
import aiss.gitminer.model.User;
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
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "Issue", description = "Issue management API")
@RestController
@RequestMapping("/gitminer")
public class IssueController {

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    ProjectRepository projectRepository;

    /*      AÑADIDOS:
    *   - Orden
    *   - Encontrar author y assigne de issue
    *   - Paginacion al obtener todas las issues
    *   - Crear, borrar y actualizar issue
    * */

    @Operation(
            summary = "Retrive all issues",
            description = "Get a list of Issue objects",
            tags = {"issues", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Issues List",
                    content = {@Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json")}),
    })
    @GetMapping("/issues")
    public List<Issue> getAllIssues(@Parameter(description = "id of author to be filtered")@RequestParam(required = false) String authorId,
                                    @Parameter(description = "state to be filtered")@RequestParam(required = false) String state,
                                    @Parameter(description = "sort parameter") @RequestParam(required = false) String order,
                                    @Parameter(description = "number of page") @RequestParam(defaultValue = "0") Integer page,
                                    @Parameter(description = "size of page") @RequestParam(defaultValue = "10") Integer size){
        Pageable paging;

        if(order != null){
            if(order.startsWith("-"))
                paging = PageRequest.of(page, size, Sort.by(order.substring(1)).descending());
            else
                paging = PageRequest.of(page, size, Sort.by(order).ascending());
        } else
            paging = PageRequest.of(page, size);

        Page<Issue> pageIssue;

        if(authorId!=null && state!=null){
            pageIssue = issueRepository.findByAuthorIdAndState(authorId, state, paging);
        } else if(authorId!=null) {
            pageIssue = issueRepository.findByAuthorId(authorId, paging);
        } else if(state!=null) {
            pageIssue = issueRepository.findByState(state, paging);
        } else {
            pageIssue = issueRepository.findAll(paging);
        }

        return pageIssue.getContent();
    }

    @Operation(
            summary = "Retrive an Issues by Id",
            description = "Get a Issue object by specifying its id",
            tags = {"issues", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Issue found", content = {@Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Issue not found", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/issues/{id}")
    public Issue getIssueById(@Parameter(description = "id of issue to be searched") @PathVariable String id) throws IssueNotFoundException{
        if(!issueRepository.existsById(id)){
            throw new IssueNotFoundException();
        }
        return issueRepository.findById(id).get();
    }

    @Operation(
            summary = "Retrive Comments of Issue by Id",
            description = "Get a list of Comment object by specifying his Issue id",
            tags = {"issues", "get", "comments"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comments of Issue found", content = {@Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Issue not found", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/issues/{id}/comments")
    public List<Comment> getCommentsOfIssue(@Parameter(description = "id of issue to be searched") @PathVariable String id) throws IssueNotFoundException {
        return getIssueById(id).getComments();
    }

    @Operation(
            summary = "Retrive Author of Issue by Id",
            description = "Get a User object by specifying his Issue id",
            tags = {"issues", "get", "users", "author"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author of Issue found", content = {@Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Issue not found", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/issues/{id}/author")
    public User getIssueAuthor(@Parameter(description = "id of issue to be searched") @PathVariable String id) throws IssueNotFoundException {
        return getIssueById(id).getAuthor();
    }

    @Operation(
            summary = "Retrive Assignee of Issue by Id",
            description = "Get a User object by specifying his Issue id",
            tags = {"issues", "get", "users", "assignee"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assignee of Issue found", content = {@Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Issue not found", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/issues/{id}/assignee")
    public User getIssueAssignee(@Parameter(description = "id of issue to be searched") @PathVariable String id) throws IssueNotFoundException {
        return getIssueById(id).getAssignee();
    }

    @Operation(
            summary = "Insert Issue in Project by Id",
            description = "Create a Issue with the body of the request",
            tags = {"issues", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Issue has been created", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Issue could not be created", content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/projects/{projectId}/issues")
    public void createIssue(@Parameter(description = "id of project to be searched") @PathVariable String projectId,
                            @Parameter(description = "issue to be created in the project") @RequestBody @Valid Issue issue) throws ProjectNotFoundException {
        if(!projectRepository.existsById(projectId)){
            throw new ProjectNotFoundException();
        }

        Project project = projectRepository.findById(projectId).get();

        project.getIssues().add(issue);

        projectRepository.save(project);
    }

    @Operation(
            summary = "Change a Issue by Id",
            description = "Update Issue by Id",
            tags = {"issues", "put"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Issue has been updated", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Issue could not be updated", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Issue not found", content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/issues/{id}")
    public void updateIssue(@Parameter(description = "id of issue to be updated") @PathVariable String id,
                            @Parameter(description = "body with issue changes") @RequestBody @Valid Issue issue) throws IssueNotFoundException {
        if(!issueRepository.existsById(id)){
            throw new IssueNotFoundException();
        }
        issue.setId(id);
        issueRepository.save(issue);
    }

    @Operation(
            summary = "Erease a Issue by Id",
            description = "Delete a Issue by Id",
            tags = {"issues", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Issue has been deleted", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Issue could not to be deleted", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Issue not found", content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/issues/{id}")
    public void deleteIssue(@Parameter(description = "id of issue to be deleted") @PathVariable String id) throws IssueNotFoundException {
        if(!issueRepository.existsById(id)){
            throw new IssueNotFoundException();
        }
        issueRepository.deleteById(id);
    }

}
