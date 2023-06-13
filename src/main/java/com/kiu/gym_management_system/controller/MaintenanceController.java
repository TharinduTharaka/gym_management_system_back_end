package com.kiu.gym_management_system.controller;


import com.kiu.gym_management_system.model.maintenance.MaintenanceModel;
import com.kiu.gym_management_system.model.schedule.ScheduleModel;
import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.impl.MaintenanceManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gym/maintenance")
@CrossOrigin(origins = "*")
public class MaintenanceController {

    @Autowired
    private MaintenanceManagerService maintenanceManagerService;

    @GetMapping("/get-all-maintenance/{emp_id}")
    public Response getAllMaintenance(@PathVariable String emp_id) {
        return maintenanceManagerService.getAllMaintenance(emp_id);
    }

    @GetMapping("/get-all-maintenance-category/")
    public Response getAllMaintenanceCategory() {
        return maintenanceManagerService.getAllMaintenanceCategory();
    }

    @GetMapping("/get-all-admin-maintenance/{id}")
    public Response getMaintenance(@PathVariable int id) {
        return maintenanceManagerService.getMaintenance(id);
    }


    @GetMapping("/get-user-filter-maintenance")
    public Response getFilterMaintenance(@RequestParam(value = "status") int status) {
        return maintenanceManagerService.getFilterMaintenance(status);
    }

    @PostMapping("/admin/create-maintenance/{emp_id}")
    public Response createMaintenance(@PathVariable String emp_id,
                                      @RequestBody MaintenanceModel maintenanceModel) {
        return maintenanceManagerService.createMaintenance(emp_id, maintenanceModel);
    }

    @PutMapping("/admin/update-maintenance/{emp_id}/{id}")
    public Response editMaintenance(@PathVariable String emp_id, @PathVariable int id,
                                    @RequestBody MaintenanceModel maintenanceModel) {
        return maintenanceManagerService.editMaintenance(emp_id, maintenanceModel, id);
    }

    @PutMapping("/delete-maintenance/{id}/{emp_id}")
    public Response deleteMaintenance(@PathVariable int id,
                                      @PathVariable String emp_id,
                                      @RequestParam(value = "status") int status) {
        return maintenanceManagerService.deleteMaintenance(status, id, emp_id);
    }

}
