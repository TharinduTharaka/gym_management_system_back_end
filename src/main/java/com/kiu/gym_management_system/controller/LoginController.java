package com.kiu.gym_management_system.controller;

import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.impl.LoginManagerService;
import com.kiu.gym_management_system.service.impl.ProfileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gym")
@CrossOrigin(origins = "*")
public class LoginController {


    @Autowired
    LoginManagerService loginManagerService;
    @GetMapping("/login")
    public Response getUserProfileDetails(@RequestParam(value = "email") String email,
                                          @RequestParam(value = "password") String password) {
        return loginManagerService.getUserLoginDetails(email,password);
    }
}
