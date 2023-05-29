package com.kiu.gym_management_system.service;

import com.kiu.gym_management_system.response.Response;

public interface AttendanceManager {


    Response getAllAttendance();
    Response getUserAttendance(String empID, String role);

    Response getAllUserAttendance(String role);
}
