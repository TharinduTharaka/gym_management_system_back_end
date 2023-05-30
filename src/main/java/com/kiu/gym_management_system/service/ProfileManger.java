package com.kiu.gym_management_system.service;

import com.kiu.gym_management_system.response.Response;

public interface ProfileManger {
    Response getUserProfileDetails(int id);
    Response getAllUserProfileDetails();
    Response getUserProfileDetailsByStatus(int status);

    Response getDataForFilterAdmin(int status);

    Response approvedUser(int status,int id);
}
