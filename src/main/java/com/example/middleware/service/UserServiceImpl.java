/**
 * Created By shoh@n
 * Date: 3/27/2023
 */

package com.example.middleware.service;

import com.example.middleware.entity.User;
import com.example.middleware.exception.ApiException;
import com.example.middleware.model.UserRequest;
import com.example.middleware.model.UserResponse;
import com.example.middleware.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getUserByUUID(String uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        return new UserResponse(user.getUuid(), user.getEmail());
    }

    @Override
    public UserResponse updateUserByUUID(String uuid, UserRequest request) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        user.setEmail(request.email());
        user = userRepository.save(user);
        return new UserResponse(user.getUuid(), user.getEmail());
    }

    @Override
    public void deleteUserByUUID(String uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        userRepository.delete(user);
    }
}
