package com.kiu.gym_management_system.service;

import com.kiu.gym_management_system.model.schedule.ScheduleModel;
import com.kiu.gym_management_system.response.Response;

public interface ScheduleManager {

    Response getAllSchedule();
    Response getUserAllSchedule(String empID);
    Response getUserSchedule(String empID, int id);

    Response getUserFilterScheduleData(String empID, int status);
    Response getAdminFilterScheduleData(int status);
    Response createSchedule(String empID, ScheduleModel scheduleModel);
    Response editSchedule(ScheduleModel scheduleModel, int id);
    Response editScheduleStatus(int status, int id);
    Response deleteSchedule(int status, int id,String empID);
}
