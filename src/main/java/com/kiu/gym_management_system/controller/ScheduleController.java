package com.kiu.gym_management_system.controller;

import com.kiu.gym_management_system.model.reservation.ReservationModel;
import com.kiu.gym_management_system.model.schedule.ScheduleModel;
import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.impl.ReservationManagerService;
import com.kiu.gym_management_system.service.impl.ScheduleManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gym/schedule")
@CrossOrigin(origins = "*")
public class ScheduleController {

    @Autowired
    private ScheduleManagerService scheduleManagerService;

    @GetMapping("/get-all-schedule")
    public Response getAllSchedule() {
        return scheduleManagerService.getAllSchedule();
    }

    @GetMapping("/get-admin-schedule/{id}")
    public Response getSchedule(@PathVariable int id) {
        return scheduleManagerService.getSchedule(id);
    }

    @GetMapping("/get-user-all-schedule/{emp_id}")
    public Response getUserAllSchedule(@PathVariable String emp_id) {
        return scheduleManagerService.getUserAllSchedule(emp_id);
    }

    @GetMapping("/get-user-schedule/{emp_id}/{id}")
    public Response getUserSchedule(@PathVariable String emp_id,
                                    @PathVariable int id) {
        return scheduleManagerService.getUserSchedule(emp_id, id);
    }

    @GetMapping("/get-user-filter-schedule/{emp_id}")
    public Response getUserFilterReservationData(@PathVariable String emp_id,
                                                 @RequestParam(value = "status") int status) {
        return scheduleManagerService.getUserFilterScheduleData(emp_id, status);
    }
    @GetMapping("/get-admin-filter-schedule")
    public Response getUserFilterReservationData(@RequestParam(value = "status") int status) {
        return scheduleManagerService.getAdminFilterScheduleData(status);
    }

    @PostMapping("/create-schedule/{emp_id}")
    public Response createSchedule(@PathVariable String emp_id,
                                   @RequestBody ScheduleModel scheduleModel) {
        return scheduleManagerService.createSchedule(emp_id, scheduleModel);
    }

    @PutMapping("/update-schedule-status/{id}")
    public Response editScheduleStatus(@PathVariable int id,
                                 @RequestParam(value = "status") int status) {
        return scheduleManagerService.editScheduleStatus(status, id);
    }
    @PutMapping("/admin/update-schedule/{id}")
    public Response editSchedule(@PathVariable int id,
                                 @RequestBody ScheduleModel scheduleModel) {
        return scheduleManagerService.editSchedule(scheduleModel, id);
    }


    @PutMapping("/delete-schedule/{id}")
    public Response deleteSchedule(@PathVariable int id,
                                   @RequestParam(value = "status") int status) {
        return scheduleManagerService.deleteSchedule(status, id);
    }
}
