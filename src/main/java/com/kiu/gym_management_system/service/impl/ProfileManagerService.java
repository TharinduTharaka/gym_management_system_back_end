package com.kiu.gym_management_system.service.impl;

import com.kiu.gym_management_system.repository.ProfileRepository;
import com.kiu.gym_management_system.repository.UserRepository;
import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.ProfileManger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileManagerService implements ProfileManger {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    UserRepository userRepository;
    @Override
    public Response getUserProfileDetails(int id) {
        Response response = new Response();
        response.setCode(200);
        response.setMsg("Get User Profile Details");
        response.setData(profileRepository.findById(id));
        return response;
    }
}
