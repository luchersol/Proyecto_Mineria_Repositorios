

































































package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.Project;
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

import javax.swing.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;




@Tag(name = "Project", description = "Project management API")
@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {
    @Autowired
    ProjectRepository projectRepository;

    @Operation(summary = "Retrieve all projects", description = "Get all projects. You can search by name and order the results", tags = { "projects", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Project.class), mediaType = "application/json") })
    })
    @GetMapping
    public List<Project> findAll(@Parameter(description = "The name you want to search") @RequestParam(required = false) String name,
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
        }else {
            paging = PageRequest.of(page, size);
        }
        Page<Project> pageProject;
        if(name != null){
            pageProject = projectRepository.findByNameContaining(name, paging);
        } else{
            pageProject = projectRepository.findAll(paging);
        }
        return pageProject.getContent();
    }



    @Operation(summary = "Retrieve a project given its id", description = "Get a project once its id is given", tags = { "projects", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project found", content = { @Content(schema = @Schema(implementation = Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description="Project not found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/{id}")
    public Project findOne(
            @Parameter(description = "id of the project that we want to see")
            @PathVariable String id) throws ProjectNotFoundException {
        Optional <Project>  project = projectRepository.findById(id);
        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        return project.get();
    }


    @Operation(summary = "Post a project", description = "Post the project", tags = { "projects", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project created", content = { @Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json") }),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@Parameter(description = "data of the new project that we want to post") @RequestBody @Valid Project project) {
        return projectRepository.save(project);
    }


    @Operation(summary = "Update a project once its given its id", description = "Updtate the project which id is given", tags = { "projects", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Project updated", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description="Project not found", content = { @Content(schema = @Schema()) })
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProject(@Parameter(description = "id of the project that we want to update") @PathVariable String id,
                              @Parameter(description = "new values of the project we want to update") @RequestBody @Valid Project newProject) throws ProjectNotFoundException {
        Optional <Project> project = projectRepository.findById(id);
        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        project.get().setCommits(newProject.getCommits());
        project.get().setIssues(newProject.getIssues());
        project.get().setName(newProject.getName());
        project.get().setWebUrl(newProject.getWebUrl());
        projectRepository.save(project.get());
    }


    @Operation(summary = "Delete a project once its given its id", description = "Delete the project which id is given", tags = { "projects", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Project deleted", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description="Project not found", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(
            @Parameter(description = "id of the project that we want to delete")
           @PathVariable  String id) throws ProjectNotFoundException {

        if(!projectRepository.existsById(id)){
            throw new ProjectNotFoundException();
        }
        projectRepository.deleteById(id);
    }

}
