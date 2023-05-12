package com.kiu.gym_management_system.service;

import com.kiu.gym_management_system.response.Response;

public interface ScheduleManager {

    Response getAllSchedule(String empID);

    Response getAllUserReservation(String empID);
    Response createReservation(String empID);
    Response editReservation(String empID, int id);
    Response deleteReservation(String empID, int id);
}
