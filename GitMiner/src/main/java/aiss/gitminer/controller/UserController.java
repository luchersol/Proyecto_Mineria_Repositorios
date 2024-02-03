package aiss.gitminer.controller;

import aiss.gitminer.exception.UserNotFoundException;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.User;
import aiss.gitminer.repository.UserRepository;
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

@Tag(name = "User", description = "User management API")
@RestController
@RequestMapping("/gitminer/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Operation(
            summary = "Retrive all users",
            description = "Get a list of User objects",
            tags = {"users", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User List", content = {@Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json")}),
    })
    @GetMapping
    public List<User> getAllUsers(
            @Parameter(description = "Username that you are looking for") @RequestParam(required = false) String username,
            @Parameter(description = "Name that you are looking for") @RequestParam(required = false) String name,
            @Parameter(description = "Sort parameter") @RequestParam(required = false) String order,
            @Parameter(description = "Number of page") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Size of page") @RequestParam(defaultValue = "10") Integer size){

        Pageable paging;
        if(order != null){
            if(order.startsWith("-")) {
                paging = PageRequest.of(page, size, Sort.by(order.substring(1)).descending());
            } else{
                paging = PageRequest.of(page, size, Sort.by(order).ascending());
            }
        } else {
            paging = PageRequest.of(page, size);
        }

        Page<User> userPage;
        if(username != null && name != null){
            userPage = userRepository.findByUsernameAndNameContaining(username,name,paging);
        } else if(username != null){
            userPage = userRepository.findByUsername(username,paging);
        } else if (name != null) {
            userPage = userRepository.findByNameContaining(name,paging);
        }else{
            userPage = userRepository.findAll(paging);
        }

        return userPage.getContent();
    }

    @Operation(
            summary = "Retrive an User by Id",
            description = "Get a User object by specifying its id",
            tags = {"users", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found", content = {@Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) throws UserNotFoundException {
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException();
        }
        return userRepository.findById(id).get();
    }

    @Operation(
            summary = "Insert a User",
            description = "Create a User with the body of the request",
            tags = {"users", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Issue has been created", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Issue could not be created", content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User createUser(@RequestBody @Valid User user){
        return userRepository.save(user);
    }

    @Operation(
            summary = "Change a User by Id",
            description = "Update User by Id",
            tags = {"users", "put"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User has been updated", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "User could not be updated", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateUser(@PathVariable String id, @RequestBody @Valid User updatedUser) throws UserNotFoundException{
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException();
        }
        updatedUser.setId(id);
        userRepository.save(updatedUser);
    }

    @Operation(
            summary = "Erease a User by Id",
            description = "Delete a User by Id",
            tags = {"users", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User has been deleted", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "User could not to be deleted", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) throws UserNotFoundException{
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }


}
