package com.personal.match_time.User.Api;

import com.personal.match_time.User.Model.User;
import com.personal.match_time.User.Request.UserRequest;
import com.personal.match_time.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {

        return userService.getAllUsers();
    }

    @PostMapping("/save")
    public User storeUser(
        @Validated @RequestBody UserRequest userRequest
    ) {

        return userService.storeUser(userRequest);
    }
}
