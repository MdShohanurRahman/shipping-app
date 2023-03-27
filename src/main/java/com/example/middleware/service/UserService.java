/**
 * Created By shoh@n
 * Date: 3/27/2023
 */

package com.example.middleware.service;

import com.example.middleware.model.UserRequest;
import com.example.middleware.model.UserResponse;

public interface UserService {

    UserResponse getUserByUUID(String uuid);

    UserResponse updateUserByUUID(String uuid, UserRequest request);

    void deleteUserByUUID(String uuid);
}
