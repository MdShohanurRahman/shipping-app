/**
 * Created By shoh@n
 * Date: 3/27/2023
 */

package com.example.middleware.controller;

import com.example.middleware.model.UserRequest;
import com.example.middleware.model.UserResponse;
import com.example.middleware.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserResponse> getUserByUUID(@PathVariable String uuid) {
        return ResponseEntity.ok(userService.getUserByUUID(uuid));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UserResponse> updateUserByUUID(@PathVariable String uuid, @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUserByUUID(uuid, request));
    }

    @DeleteMapping("/{uuid}")
    public void deleteUserByUUID(@PathVariable String uuid) {
        userService.deleteUserByUUID(uuid);
    }
}
