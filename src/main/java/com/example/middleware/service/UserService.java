/**
 * Created By shoh@n
 * Date: 3/27/2023
 */

package com.example.middleware.service;

import com.example.middleware.model.UserRequest;
import com.example.middleware.model.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getUsers();

    UserResponse getUserByUUID(String uuid);

    UserResponse updateUserByUUID(String uuid, UserRequest request);

    void deleteUserByUUID(String uuid);
}
