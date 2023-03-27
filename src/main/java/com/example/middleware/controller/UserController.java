/**
 * Created By shoh@n
 * Date: 3/27/2023
 */

package com.example.middleware.controller;

import com.example.middleware.model.UserRequest;
import com.example.middleware.model.UserResponse;
import com.example.middleware.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{uuid}")
    public UserResponse getUserByUUID(@PathVariable String uuid) {
        return userService.getUserByUUID(uuid);
    }

    @PutMapping("/{uuid}")
    public UserResponse updateUserByUUID(@PathVariable String uuid, @RequestBody UserRequest request) {
        return userService.updateUserByUUID(uuid, request);
    }

    @DeleteMapping("/{uuid}")
    public void deleteUserByUUID(@PathVariable String uuid) {
        userService.deleteUserByUUID(uuid);
    }
}
