package com.kiu.gym_management_system.controller;

import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.impl.UserManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gym/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserManagerService userManagerService;
    @GetMapping("/get-all-user-details/{role}")
    public Response getAllUsersByRole(@PathVariable String role) {
        return userManagerService.getAllUsersByRole(role);
    }
}
