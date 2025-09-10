package com.personal.match_time.user.api;

import com.personal.match_time.user.model.User;
import com.personal.match_time.user.request.UserRequest;
import com.personal.match_time.user.service.UserService;
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
