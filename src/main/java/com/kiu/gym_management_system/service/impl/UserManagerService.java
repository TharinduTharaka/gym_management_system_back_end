package com.kiu.gym_management_system.service.impl;

import com.kiu.gym_management_system.repository.LoginRepository;
import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManagerService implements UserManager {

    @Autowired
    LoginRepository loginRepository;
    @Override
    public Response getAllUsersByRole(String role){
        Response response = new Response();
        response.setCode(200);
        response.setData(loginRepository.findByRole(role));
        response.setMsg("Get user's All Schedule");
        return response;
    }
}
