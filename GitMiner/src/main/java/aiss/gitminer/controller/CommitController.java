package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.CommitRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Tag(name = "Commit", description = "Commit management API")
@RestController
@RequestMapping("/gitminer")
public class CommitController {
    @Autowired
    CommitRepository commitRepository;

@Autowired
ProjectRepository projectRepository;

    @Operation(
            summary = "Retrieve a list of commits",
            description = "Get a list of commits. You can filter, page and order.",
            tags = {"commits","get"}
    )
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Commit.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", description="Commits not found", content = { @Content(schema = @Schema()) })
    @GetMapping("/commits")
    public List<Commit> getAll(@Parameter(description = "Name of the author")@RequestParam(required = false) String authorName,
                               @Parameter(description = "email of the author of the commit")@RequestParam(name = "email", required = false) String authorEmail,
                               @Parameter(description = "Sort parameter")@RequestParam(required = false) String order,
                               @Parameter(description = "Number of pages")@RequestParam(defaultValue = "0") int page,
                               @Parameter(description = "Size of pages")@RequestParam(defaultValue = "10") int size) throws CommitNotFoundException {
        Page<Commit> pageCommit;
        Pageable paging;
        // order
        if(order != null) {
            if (order.startsWith("-"))
                paging = PageRequest.of(page, size, Sort.by(order.substring(1)).descending());
            else
                paging = PageRequest.of(page, size, Sort.by(order).ascending());
        }
        else {
            paging = PageRequest.of(page, size);
        }
        // filter
        if (authorName != null && authorEmail !=null) {
            pageCommit = commitRepository.findByAuthorNameAndAuthorEmail(authorName, authorEmail, paging);
        } else if(authorName != null) {
            pageCommit = commitRepository.findByAuthorName(authorName, paging);
        } else if(authorEmail != null) {
            pageCommit = commitRepository.findByAuthorEmail(authorEmail, paging);
        } else {
            pageCommit = commitRepository.findAll(paging);
        }
        return pageCommit.getContent();
    }

    @Operation(
            summary = "Retrieve a commit given its id ",
            description = "Get a commit",
            tags = {"commits","get"}
    )
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Commit.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", description="Projects not found", content = { @Content(schema = @Schema()) })
    @GetMapping("/commits/{commitId}")
    public Commit getCommitsById( @Parameter(description = "id of the commit that we want to see")
            @PathVariable(value = "commitId") String commitId) throws CommitNotFoundException {

        Optional<Commit> commit = commitRepository.findById(commitId);
        if (!commit.isPresent()){
            throw new CommitNotFoundException();
        };

        return commit.get();
    }

    @Operation(summary = "Create a commit given its projectId", description = "Create a commit", tags = { "commits", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Commit created", content = { @Content(schema = @Schema(implementation = Commit.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Project not found", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("/projects/{projectId}/commits")
    @ResponseStatus(HttpStatus.CREATED)
    public Commit create(@RequestBody @Valid Commit commit,
                         @Parameter(description = "Project id")@PathVariable("projectId") String projectId) throws ProjectNotFoundException {
        Optional<Project> project = projectRepository.findById(projectId);
        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        project.get().getCommits().add(commit);
        projectRepository.save(project.get());
        return commit;
    }


    @Operation(summary = "Update a commit given its commitId", description = "Update a commit", tags = { "commits", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Commit updated", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description="Commit not found", content = { @Content(schema = @Schema()) })
    })
    @PutMapping("/commits/{commitId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Commit updatedCommit,
                       @Parameter(description = "Commit id")@PathVariable String commitId) throws CommitNotFoundException {

        Optional<Commit> cmData = commitRepository.findById(commitId);
        if(!cmData.isPresent()) {
            throw new CommitNotFoundException();
        }
        Commit commit = cmData.get();
        commit.setAuthorEmail(updatedCommit.getAuthorEmail());
        commit.setMessage(updatedCommit.getMessage());
        commit.setTitle(updatedCommit.getTitle());
        commit.setAuthoredDate(updatedCommit.getAuthoredDate());
        commitRepository.save(commit);
    }




    @Operation(summary = "Delete a commit given its commitId", description = "Delete a commit", tags = { "commits", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Commit deleted", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description="Commit not found", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("/commits/{commitId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "Commit id")@PathVariable String commitId) throws CommitNotFoundException {
        if(!commitRepository.existsById(commitId)){
            throw new CommitNotFoundException();
        }
            commitRepository.deleteById(commitId);

    }



}
