package com.kiu.gym_management_system.service.impl;

import com.kiu.gym_management_system.repository.UserRepository;
import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.LoginManager;
import com.kiu.gym_management_system.service.ProfileManger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginManagerService implements LoginManager {


    @Autowired
    UserRepository userRepository;

    @Override
    public Response getUserLoginDetails(String email, String password) {
        Response response = new Response();
        response.setCode(200);
        response.setMsg("Get User Login Details");
        response.setData(userRepository.findByEmail(email));
        return response;
    }
}
