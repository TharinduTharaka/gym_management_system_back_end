package com.kiu.gym_management_system.service;

import com.kiu.gym_management_system.model.maintenance.MaintenanceModel;
import com.kiu.gym_management_system.model.schedule.ScheduleModel;
import com.kiu.gym_management_system.response.Response;

public interface MaintenanceManager {
    Response getAllMaintenance(String emp_id);

    Response getAllMaintenanceCategory();

    Response getMaintenance(int id);

    Response getFilterMaintenance(int status);
    Response createMaintenance(String empID, MaintenanceModel maintenanceModel);

    Response editMaintenance(String empID,MaintenanceModel maintenanceModel, int id);

    Response deleteMaintenance(int status, int id,String empID);

}
