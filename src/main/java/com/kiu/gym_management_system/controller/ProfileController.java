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

    @GetMapping("/get-all-user-profile-details")
    public Response getAllUserProfileDetails() {
        return profileManagerService.getAllUserProfileDetails();
    }

    @GetMapping("/get-user-profile-details-byStatus/{status}")
    public Response getUserProfileDetailsByStatus(@PathVariable int status) {
        return profileManagerService.getUserProfileDetailsByStatus(status);
    }

    @GetMapping("/get-admin-filter-profile-details")
    public Response getDataForFilterAdmin(@RequestParam(value = "status") int status) {
        return profileManagerService.getDataForFilterAdmin(status);
    }

    @PutMapping("/approve-user/{id}")
    public Response editScheduleStatus(@PathVariable int id,
                                       @RequestParam(value = "status") int status) {
        return profileManagerService.approvedUser(status, id);
    }
}
