package com.kiu.gym_management_system.controller;

import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.impl.ProfileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gym/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    ProfileManagerService profileManagerService;


    @GetMapping("/get-user-profile-details/{id}")
    public Response getUserProfileDetails(@PathVariable int id) {
        return profileManagerService.getUserProfileDetails(id);
    }
}
