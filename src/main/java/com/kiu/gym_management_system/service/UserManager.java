package com.kiu.gym_management_system.service;

import com.kiu.gym_management_system.response.Response;

public interface UserManager {
    Response getAllUsersByRole(String role);
}
