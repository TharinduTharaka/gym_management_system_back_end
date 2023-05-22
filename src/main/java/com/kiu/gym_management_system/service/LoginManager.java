package com.kiu.gym_management_system.service;

import com.kiu.gym_management_system.response.Response;

public interface LoginManager {
    Response getUserLoginDetails(String email, String password);
}
